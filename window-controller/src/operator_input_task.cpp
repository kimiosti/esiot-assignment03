#include <Arduino.h>
#include "operator_input/operator_input_task.h"
#include "operator_input/tactile_button.h"
#include "operator_input/potentiometer.h"

#define BUTTON_PIN 8
#define KNOB_PIN A0

OperatorInputTask::OperatorInputTask(long period, DirtyStateTracker *dirty_state_tracker) {
    this->period = period;
    this->step_count = 0;
    this->dirty_state_tracker = dirty_state_tracker;
    this->button = new TactileButton(BUTTON_PIN);
    this->knob = new Potentiometer(KNOB_PIN);
}

void OperatorInputTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period > this->period) {
        this->step_count = 0;
        if (this->button->isPressed()) {
            this->dirty_state_tracker->requestModeSwitch();
        }

        this->dirty_state_tracker->setOpeningPercentage(this->knob->readPercentage());
    }
}