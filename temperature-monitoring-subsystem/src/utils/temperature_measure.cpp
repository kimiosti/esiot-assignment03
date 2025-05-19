#include "utils/temperature_measure.h"

TemperatureMeasure::TemperatureMeasure(float temperature, String date, String time) {
    this->temperature = temperature;
    this->date = date;
    this->time = time;
}

float TemperatureMeasure::getTemperature() {
    return this->temperature;
}

String TemperatureMeasure::getDate() {
    return this->date;
}

String TemperatureMeasure::getTime() {
    return this->time;
}