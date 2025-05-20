#ifndef __LED_TASK__
#define __LED_TASK__

#include "task.h"
#include "device/led.h"
#include "utils/system_state_tracker.h"

class LEDTask: public Task {
    private:
        long period;
        LED *green;
        LED *red;
        SystemStateTracker *stateTracker;
        SemaphoreHandle_t sharedDataMutex;

    public:
        LEDTask(long period, SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex);
        void run(void *params);
        void update();
};

#endif