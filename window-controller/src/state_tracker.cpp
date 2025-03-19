#include "state_tracker.h"

StateTracker::StateTracker() {
    this->temperature = 0.0;
    this->openingPercentage = 0;
    this->mode = AUTOMATIC;
}

float StateTracker::getTemperature() {
    return this->temperature;
}

int StateTracker::getOpeningPercentage() {
    return this->openingPercentage;
}

void StateTracker::setOpeningPercentage(int openingPercentage) {
    this->openingPercentage = openingPercentage;
}

Mode StateTracker::getMode() {
    return this->mode;
}

void StateTracker::setMode(Mode mode) {
    this->mode = mode;
}
