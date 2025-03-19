#include "communication/communication_task.h"

CommunicationTask::CommunicationTask(long period, DirtyStateTracker *dirty_state_tracker, StateTracker *state_tracker) {
    this->step_count = 0;
    this->period = period;
    this->dirty_state_tracker = dirty_state_tracker;
    this->state_tracker = state_tracker;
}

void CommunicationTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period >= this->period) {
        // TODO - implement method logic.
    }
}