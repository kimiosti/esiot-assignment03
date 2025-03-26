#ifndef __WINDOW__
#define __WINDOW__

class Door {
    public:
        virtual void lock() = 0;
        virtual void unlock() = 0;
        virtual void openToLevel(int percentage) = 0;
};

#endif