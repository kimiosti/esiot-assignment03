#include "coop_r_r_scheduler.h"

CoopRRScheduler::CoopRRScheduler(long period) {
    this->period = period;
    this->task_occupation = 0;
    this->timer = new TimerImpl();
}

void CoopRRScheduler::bind(Task* task) {
    if (task_occupation < CAPACITY) {
        this->tasks[task_occupation] = task;
        task_occupation++;
    }
}

void CoopRRScheduler::schedule() {
    timer->waitForNextTick(period);
    for (int i = 0; i < task_occupation; i++) {
        tasks[i]->step(this->period);
    }
}