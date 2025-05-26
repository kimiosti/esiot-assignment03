#include <Arduino.h>
#include "utils/system_state_tracker.h"
#include "task/temperature_measuring_task.h"
#include "task/network_monitoring_task.h"
#include "task/communication_task.h"
#include "task/led_task.h"

#define RED_LED_PIN 5
#define GREEN_LED_PIN 4
#define TEMP_SENSOR_PIN 6

#define DEFAULT_STACK_DEPTH 10000
#define DEFAULT_TASK_PRIORITY 1
#define DEFAULT_TASK_ARGS NULL

#define NETWORK_TASK_PERIOD 500
#define LED_TASK_PERIOD 500

SystemStateTracker *stateTracker;
SemaphoreHandle_t stateTrackerMutex;

WiFiClient *wifiClient;
PubSubClient *mqttClient;

TemperatureMeasuringTask *temperatureTask;
TaskHandle_t *tempTaskHandler;

NetworkMonitoringTask *networkTask;
TaskHandle_t *networkTaskHandler;

CommunicationTask *communicationTask;
TaskHandle_t *communicationTaskHandler;

LEDTask *ledTask;
TaskHandle_t *ledTaskHandler;

void runTask(void *task) {
  ((Task *)task)->run(DEFAULT_TASK_ARGS);
}

void callback(char* topic, byte* payload, unsigned int length) {
  communicationTask->onReceive(topic, payload, length);
}

void setup() {
  // put your setup code here, to run once:
  stateTracker = new SystemStateTracker();
  stateTrackerMutex = xSemaphoreCreateMutex();

  wifiClient = new WiFiClient();
  mqttClient = new PubSubClient(*wifiClient);

  temperatureTask = new TemperatureMeasuringTask(TEMP_SENSOR_PIN, stateTracker, stateTrackerMutex);
  tempTaskHandler = new TaskHandle_t();
  xTaskCreate(runTask, "TempMeasuringTask", DEFAULT_STACK_DEPTH, (void*)temperatureTask, DEFAULT_TASK_PRIORITY, tempTaskHandler);

  networkTask = new NetworkMonitoringTask(
    NETWORK_TASK_PERIOD,
    wifiClient,
    mqttClient,
    stateTracker,
    stateTrackerMutex
  );
  networkTaskHandler = new TaskHandle_t();
  xTaskCreatePinnedToCore(
    runTask,
    "NetworkMonitoringTask",
    DEFAULT_STACK_DEPTH,
    (void*)networkTask,
    DEFAULT_TASK_PRIORITY,
    networkTaskHandler,
    0
  );

  mqttClient->setCallback(callback);
  communicationTask = new CommunicationTask(stateTracker, stateTrackerMutex, mqttClient);
  communicationTaskHandler = new TaskHandle_t();
  xTaskCreatePinnedToCore(
    runTask, "CommTask", DEFAULT_STACK_DEPTH, (void*)communicationTask, DEFAULT_TASK_PRIORITY, communicationTaskHandler, 0
  );

  ledTask = new LEDTask(LED_TASK_PERIOD, RED_LED_PIN, GREEN_LED_PIN, stateTracker, stateTrackerMutex);
  ledTaskHandler = new TaskHandle_t();
  xTaskCreate(runTask, "LEDTask", DEFAULT_STACK_DEPTH, (void*)ledTask, DEFAULT_TASK_PRIORITY, ledTaskHandler);
}

void loop() {
  // put your main code here, to run repeatedly:
}