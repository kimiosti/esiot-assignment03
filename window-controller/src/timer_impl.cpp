#include "timer_impl.h"

TimerImpl::TimerImpl() {
    this->timer = new Timer();
}

void TimerImpl::waitForNextTick(long period) {
    while (this->timer->read() < period) { }
    this->timer->pause();
    this->timer->start();
}
