#ifndef __WINDOW_CONTROLLING_TASK__
#define __WINDOW_CONTROLLING_TASK__

#include "scheduler/task.h"
#include "state_tracker.h"
#include "device/window.h"

class WindowControllingTask: public Task {
    public:
        WindowControllingTask(long period, StateTracker *state_tracker);
        void step(long sched_period);

    private:
        long period;
        int step_count;
        int idle_step_count;
        StateTracker *state_tracker;
        int last_measure;
        Window *window;
};

#endif