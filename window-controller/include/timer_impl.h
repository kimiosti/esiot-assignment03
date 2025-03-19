#ifndef __TIMER_IMPL__
#define __TIMER_IMPL__

#include <Timer.h>

class TimerImpl {
    public:
        TimerImpl();
        void waitForNextTick(long period);

    private:
        Timer *timer;
};

#endif