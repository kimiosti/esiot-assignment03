#include "device/servo_motor.h"

#define MIN_PULSE 750
#define PERCENTILE_SIZE 7.5

ServoMotor::ServoMotor(int pin) {
    this->pin = pin;
    this->motor = new ServoTimer2();
}

void ServoMotor::lock() {
    this->motor->detach();
}

void ServoMotor::unlock() {
    if (!this->motor->attached()) {
        this->motor->attach(this->pin);
    }
}

void ServoMotor::openToLevel(int percentage) {
    this->motor->write(MIN_PULSE + (int) (percentage * PERCENTILE_SIZE));
}