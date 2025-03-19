#ifndef __TIMER_IMPL__
#define __TIMER_IMPL__

#include <Timer.h>

class TimerImpl {
    public:
        TimerImpl(long period);
        void setPeriod(long period);
        void waitForNextTick();

    private:
        Timer *timer;
        uint32_t period;
};

#endif