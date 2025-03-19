#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "task.h"

class Scheduler {
    public:
        virtual void bind(Task *task) = 0;
        virtual void schedule() = 0;
};

#endif