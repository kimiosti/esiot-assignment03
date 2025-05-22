#ifndef __COMMUNICATION_TASK__
#define __COMMUNICATION_TASK__

#include <PubSubClient.h>
#include "task.h"
#include "utils/system_state_tracker.h"

class CommunicationTask: public Task {
    private:
        unsigned long period;
        SystemStateTracker *stateTracker;
        SemaphoreHandle_t sharedDataMutex;
        PubSubClient *mqttClient;

    public:
        CommunicationTask(
            SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex, PubSubClient *mqttClient
        );
        void run(void *params);
        void update();
        void onReceive(char* topic, byte* payload, unsigned int length);
};

#endif