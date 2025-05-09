#ifndef __COOP_R_R_SCHEDULER__
#define __COOP_R_R_SCHEDULER__

#include "scheduler/scheduler.h"
#include "scheduler/timer_impl.h"

#define CAPACITY 8

class CoopRRScheduler : public Scheduler {
    public:
        CoopRRScheduler(long period);
        void bind(Task* task);
        void schedule();

    private:
        long period;
        int task_occupation;
        Task* tasks[CAPACITY];
        TimerImpl *timer;
};

#endif