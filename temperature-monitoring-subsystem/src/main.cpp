#include <Arduino.h>
#include "utils/system_state_tracker.h"
#include "task/led_task.h"

#define DEFAULT_STACK_DEPTH 10000
#define DEFAULT_TASK_PRIORITY 1
#define DEFAULT_TASK_ARGS NULL

#define LED_TASK_PERIOD 500

SystemStateTracker *stateTracker;
SemaphoreHandle_t stateTrackerMutex;

LEDTask *ledTask;
TaskHandle_t *ledTaskHandler;

void runTask(void *task) {
  ((Task *)task)->run(DEFAULT_TASK_ARGS);
}

void setup() {
  // put your setup code here, to run once:

  stateTracker = new SystemStateTracker();
  stateTrackerMutex = xSemaphoreCreateMutex();

  ledTask = new LEDTask(LED_TASK_PERIOD, stateTracker, stateTrackerMutex);
  ledTaskHandler = new TaskHandle_t();
  xTaskCreate(runTask, "LED Task", DEFAULT_STACK_DEPTH, (void*)ledTask, DEFAULT_TASK_PRIORITY, ledTaskHandler);
}

void loop() {
  // put your main code here, to run repeatedly:
}