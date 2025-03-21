#include "operator_output/operator_output_task.h"

#define FIRST_ROW 0
#define SECOND_ROW 1
#define THIRD_ROW 2

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
        this->screen->writeRow(FIRST_ROW, "Opening: " + this->formatPercentage());
        this->screen->writeRow(SECOND_ROW, "Mode: " + this->formatMode());
        if (this->state_tracker->getMode() == MANUAL) {
            this->screen->writeRow(THIRD_ROW, "Temperature: " + String(this->state_tracker->getTemperature(), 2U));
        }
    }
}

String OperatorOutputTask::formatPercentage() {
    int opening = this->state_tracker->getOpeningPercentage();
    String res("");
    for (int x = 100; x > 0; x /= 10) {
        res.concat(opening / x);
        opening -= (opening / x) * x;
    }
    res.concat('%');
    return res;
}

String OperatorOutputTask::formatMode() {
    if (this->state_tracker->getMode() == AUTOMATIC) return "automatic";
    else return "manual";
}
