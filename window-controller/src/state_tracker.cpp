#include "state_tracker.h"

StateTracker::StateTracker() {
    this->temperature = 0.0;
    this->opening_percentage = 0;
    this->mode = AUTOMATIC;
}

float StateTracker::getTemperature() {
    return this->temperature;
}

void StateTracker::setTemperature(float temperature) {
    this->temperature = temperature;
}

int StateTracker::getOpeningPercentage() {
    return this->opening_percentage;
}

void StateTracker::setOpeningPercentage(int opening_percentage) {
    this->opening_percentage = opening_percentage;
}

Mode StateTracker::getMode() {
    return this->mode;
}

void StateTracker::setMode(Mode mode) {
    this->mode = mode;
}
