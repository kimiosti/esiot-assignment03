#ifndef __TASK__
#define __TASK__

class Task {
    public:
        virtual void step(long sched_period) = 0;
};

#endif