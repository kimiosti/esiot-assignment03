#ifndef __NETWORK_MONITORING_TASK__
#define __NETWORK_MONITORING_TASK__

#include <WiFi.h>
#include <PubSubClient.h>
#include "task.h"
#include "utils/system_state_tracker.h"

class NetworkMonitoringTask: public Task {
    private:
        long period;
        WiFiClient *wifiClient;
        PubSubClient *mqttClient;
        SystemStateTracker *stateTracker;
        SemaphoreHandle_t sharedDataMutex;

    public:
        NetworkMonitoringTask(
            long period,
            WiFiClient *wifiClient,
            PubSubClient *mqttClient,
            SystemStateTracker *stateTracker,
            SemaphoreHandle_t sharedDataMutex
        );
        void run(void *params);
        void update();
};

#endif