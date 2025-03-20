#include "operator_output/operator_output_task.h"

#define FIRST_ROW 0
#define SECOND_ROW 1
#define THIRD_ROW 2

String formatAsPercentage(int value) {
    String res("");
    for (int x = 100; x > 0; x /= 10) {
        res.concat(value / x);
        value -= (value / x) * x;
    }
    res.concat('%');
    return res;
}

OperatorOutputTask::OperatorOutputTask(long period, StateTracker *state_tracker) {
    this->step_count = 0;
    this->period = period;
    this->screen = new UserScreen();
    this->state_tracker = state_tracker;
}

void OperatorOutputTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period >= this->period) {
        this->step_count = 0;
        this->screen->clear();
        this->screen->writeRow(FIRST_ROW, "Opening: " + formatAsPercentage(this->state_tracker->getOpeningPercentage()));
        this->screen->writeRow(SECOND_ROW, "Mode: ");
        if (this->state_tracker->getMode() == MANUAL) {
            this->screen->writeRow(THIRD_ROW, "Temperature: ");
        }
    }
}
