#ifndef __TEMPERATURE_MEASURING_TASK__
#define __TEMPERATURE_MEASURING_TASK__

#include "task.h"
#include "device/temperature_sensor.h"
#include "utils/system_state_tracker.h"

class TemperatureMeasuringTask: public Task {
    private:
        long period;
        TemperatureSensor *sensor;
        SystemStateTracker *stateTracker;
        SemaphoreHandle_t sharedDataMutex;

    public:
        TemperatureMeasuringTask(int sensorPin, SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex);
        void run(void *params);
        void update();
};

#endif