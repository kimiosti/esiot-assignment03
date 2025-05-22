#include <time.h>
#include "task/network_monitoring_task.h"

#define SSID "wifi-ssid"
#define PASSWORD "wifi-pass"
#define MQTT_BROKER "www.mqtt-dashboard.com"
#define MQTT_BROKER_PORT 1883
#define RECEIVE_TOPIC "unibo/esiot2024/temp-monitor/backend"
#define CONNECTION_TIMEOUT 1000
#define TIME_ZONE 3600
#define DAYLIGHT_SAVING 3600
#define TIME_SERVER_1 "0.pool.ntp.org"
#define TIME_SERVER_2 "1.pool.ntp.org"
#define TIME_SERVER_3 "2.pool.ntp.org"

NetworkMonitoringTask::NetworkMonitoringTask(
    long period,
    WiFiClient *wifiClient,
    PubSubClient *mqttClient,
    SystemStateTracker *stateTracker,
    SemaphoreHandle_t sharedDataMutex
) {
    this->period = period;
    this->timeSet = false;
    this->wifiClient = wifiClient;
    this->mqttClient = mqttClient;
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;
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
        this->timeSet = false;
        while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
        this->stateTracker->setOnlineStatus(false);
        this->stateTracker->setSubscriptionStatus(false);
        xSemaphoreGive(sharedDataMutex);

        WiFi.mode(WIFI_STA);
        WiFi.begin(SSID, PASSWORD);
    } else {
        if (!this->timeSet) {
            configTime(TIME_ZONE, DAYLIGHT_SAVING, TIME_SERVER_1, TIME_SERVER_2, TIME_SERVER_3);
            this->timeSet = true;
        }

        while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
        this->stateTracker->setOnlineStatus(true);
        xSemaphoreGive(sharedDataMutex);

        if (!this->mqttClient->connected()) {
            while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
            this->stateTracker->setSubscriptionStatus(false);
            xSemaphoreGive(sharedDataMutex);

            this->mqttClient->setServer(MQTT_BROKER, MQTT_BROKER_PORT);

            randomSeed(micros());
            String clientId = String("unibo/esiot2024/temp-monitor/sensor")+String(random(0xffff), HEX);

            if (this->mqttClient->connect(clientId.c_str())) {
                this->mqttClient->subscribe(RECEIVE_TOPIC);
            }
        } else {
            while(!xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period / 4))) { }
            this->stateTracker->setSubscriptionStatus(true);
            xSemaphoreGive(sharedDataMutex);
        }
    }
}