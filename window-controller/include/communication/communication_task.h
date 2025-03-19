#ifndef __COMMUNICATION_TASK__
#define __COMMUNICATION_TASK__

#include "scheduler/task.h"
#include "dirty_state_tracker.h"
#include "state_tracker.h"

class CommunicationTask: public Task {
    public:
        CommunicationTask(long period, DirtyStateTracker *dirty_state_tracker, StateTracker * state_tracker);
        void step(long sched_period);

    private:
        int step_count;
        long period;
        DirtyStateTracker *dirty_state_tracker;
        StateTracker *state_tracker;
};

#endif