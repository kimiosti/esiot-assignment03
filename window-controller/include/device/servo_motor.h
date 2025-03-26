#ifndef __SERVO_MOTOR__
#define __SERVO_MOTOR__

#include <ServoTimer2.h>
#include "window.h"

class ServoMotor: public Window {
    public:
        ServoMotor(int pin);
        void lock();
        void unlock();
        void openToLevel(int percentage);

    private:
        ServoTimer2 *motor;
        int pin;
};

#endif