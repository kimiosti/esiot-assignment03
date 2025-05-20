#include <Arduino.h>
#include "device/led.h"

LED::LED(int pin) {
    this->pin = pin;
    pinMode(this->pin, OUTPUT);
}

void LED::turnOn() {
    digitalWrite(this->pin, HIGH);
}

void LED::turnOff() {
    digitalWrite(this->pin, LOW);
}