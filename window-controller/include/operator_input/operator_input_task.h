#ifndef __OPERATOR_INPUT_TASK__
#define __OPERATOR_INPUT_TASK__

#include "scheduler/task.h"
#include "dirty_state_tracker.h"
#include "button.h"
#include "knob.h"

class OperatorInputTask: public Task {
    public:
        OperatorInputTask(long period, DirtyStateTracker *dirty_state_tracker);
        void step(long sched_period);

    private:
        long period;
        int step_count;
        DirtyStateTracker *dirty_state_tracker;
        Button *button;
        Knob *knob;
};

#endif