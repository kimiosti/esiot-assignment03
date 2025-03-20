#ifndef __OPERATOR_OUTPUT_TASK__
#define __OPERATOR_OUTPUT_TASK__

#include "scheduler/task.h"
#include "device/user_screen.h"
#include "state_tracker.h"

class OperatorOutputTask: public Task {
    public:
        OperatorOutputTask(long period, StateTracker *state_tracker);
        void step(long sched_period);

    private:
        int step_count;
        long period;
        UserScreen *screen;
        StateTracker *state_tracker;
};

#endif
