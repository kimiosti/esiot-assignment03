#include "timer_impl.h"

TimerImpl::TimerImpl(long period) {
    this->timer = new Timer();
    this->period = period;
}

void TimerImpl::setPeriod(long period) {
    this->period = period;
}

void TimerImpl::waitForNextTick() {
    while (this->timer->read() < this->period) { }
    this->timer->pause();
    this->timer->start();
}
