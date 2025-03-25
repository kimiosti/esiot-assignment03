#include <Arduino.h>
#include "operator_input/potentiometer.h"

#define MAX_READ 1023
#define MAX_VAL 100

Potentiometer::Potentiometer(int pin) {
    this->pin = pin;
    pinMode(pin, READ);
}

int Potentiometer::readPercentage() {
    return analogRead(this->pin) / MAX_READ * MAX_VAL;
}