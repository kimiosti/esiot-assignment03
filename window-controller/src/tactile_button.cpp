#include <Arduino.h>
#include "device/tactile_button.h"

TactileButton::TactileButton(int pin) {
    this->pin = pin;
    pinMode(this->pin, INPUT);
}

bool TactileButton::isPressed() {
    return digitalRead(this->pin);
}