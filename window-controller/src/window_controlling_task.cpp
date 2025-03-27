#include "window_controller/window_controlling_task.h"
#include "device/servo_motor.h"

#define WINDOW_PIN 11
#define TOLERANCE 5
#define LOCK_TIME 300

WindowControllingTask::WindowControllingTask(long period, StateTracker *state_tracker) {
    this->period = period;
    this->step_count = 0;
    this->idle_step_count = 0;
    this->state_tracker = state_tracker;
    this->last_measure = this->state_tracker->getOpeningPercentage();
    this->window = new ServoMotor(WINDOW_PIN);
}

void WindowControllingTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period >= this->period) {
        int percentage = this->state_tracker->getOpeningPercentage();
        if (abs(percentage - this->last_measure) >= TOLERANCE) {
            this->idle_step_count = 0;
            this->last_measure = percentage;
            this->window->unlock();
            this->window->openToLevel(percentage);
        } else {
            this->idle_step_count++;
        }

        if (this->idle_step_count * this->period >= LOCK_TIME) {
            this->window->lock();
        }
    }
}