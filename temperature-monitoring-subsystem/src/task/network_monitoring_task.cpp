#include <time.h>
#include "task/network_monitoring_task.h"

#define SSID "Kimbo502"
#define PASSWORD "password"
#define MQTT_BROKER "192.168.50.37"
#define MQTT_BROKER_PORT 1883
#define RECEIVE_TOPIC "unibo/esiot2024/temp-monitor/backend"

NetworkMonitoringTask::NetworkMonitoringTask(
    long period,
    WiFiClient *wifiClient,
    PubSubClient *mqttClient,
    SystemStateTracker *stateTracker,
    SemaphoreHandle_t sharedDataMutex,
    SemaphoreHandle_t networkClientsMutex
) {
    this->period = period;
    this->wifiClient = wifiClient;
    this->mqttClient = mqttClient;
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;
    this->networkClientsMutex = networkClientsMutex;
}

void NetworkMonitoringTask::run(void *params) {
    for(;;) {
        unsigned long start = millis();
        this->update();
        unsigned long elapsed = millis() - start;
        if (period > elapsed) {
           delay(period - elapsed);
        }
    }
}

void NetworkMonitoringTask::update() {
    if (WiFi.status() != WL_CONNECTED) {
        while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
        this->stateTracker->setOnlineStatus(false);
        this->stateTracker->setSubscriptionStatus(false);
        xSemaphoreGive(sharedDataMutex);

        while(!xSemaphoreTake(this->networkClientsMutex, pdMS_TO_TICKS(this->period / 4))) { }
        WiFi.mode(WIFI_STA);
        WiFi.begin(SSID, PASSWORD);
        xSemaphoreGive(this->networkClientsMutex);
    } else {
        while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
        this->stateTracker->setOnlineStatus(true);
        xSemaphoreGive(sharedDataMutex);

        if (!this->mqttClient->connected()) {
            while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
            this->stateTracker->setSubscriptionStatus(false);
            xSemaphoreGive(sharedDataMutex);

            while(!xSemaphoreTake(this->networkClientsMutex, pdMS_TO_TICKS(this->period / 4))) { }
            this->mqttClient->setServer(MQTT_BROKER, MQTT_BROKER_PORT);

            randomSeed(micros());
            String clientId = String("unibo/esiot2024/temp-monitor/sensor")+String(random(0xffff), HEX);

            if (this->mqttClient->connect(clientId.c_str())) {
                this->mqttClient->subscribe(RECEIVE_TOPIC);
            }
            xSemaphoreGive(this->networkClientsMutex);
        } else {
            while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
            this->stateTracker->setSubscriptionStatus(true);
            xSemaphoreGive(sharedDataMutex);
        }
    }
}