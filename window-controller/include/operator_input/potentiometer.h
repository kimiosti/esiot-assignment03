#ifndef __POTENTIOMETER__
#define __POTENTIOMETER__

#include "knob.h"

class Potentiometer: public Knob {
    public:
        Potentiometer(int pin);
        int readPercentage();

    private:
        int pin;
}

#endif