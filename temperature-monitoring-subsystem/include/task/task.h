#ifndef __TASK__
#define __TASK__

class Task {
    public:
        virtual void run(void* params) = 0;
};

#endif