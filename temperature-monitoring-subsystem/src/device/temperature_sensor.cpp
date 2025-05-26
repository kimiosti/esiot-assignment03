#include <Arduino.h>
#include "device/temperature_sensor.h"

#define RESOLUTION 4096.0
#define MAX_VOLTAGE 3.3
#define BASE_TEMP (-40)
#define STEP_FACTOR 100

TemperatureSensor::TemperatureSensor(int pin) {
    this->pin = pin;
}

float TemperatureSensor::getTemperature() {
    int read = analogRead(this->pin);
    float voltage = (read / RESOLUTION) * MAX_VOLTAGE;
    float temp = BASE_TEMP + (STEP_FACTOR * voltage);

    return temp;
}