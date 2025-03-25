#include <Arduino.h>
#include "operator_input/potentiometer.h"

#define MAX_READ 1023
#define MAX_VAL 100

int mapToPercentage(int value) {
    return (float) value / MAX_READ * MAX_VAL;
}

Potentiometer::Potentiometer(int pin) {
    this->pin = pin;
    pinMode(this->pin, INPUT);
}

int Potentiometer::readPercentage() {
    return (float) analogRead(this->pin) / MAX_READ * MAX_VAL;
}