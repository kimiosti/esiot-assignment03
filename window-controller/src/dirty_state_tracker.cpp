#include "dirty_state_tracker.h"

DirtyStateTracker::DirtyStateTracker() {
    this->modeSwitchRequest = 0;
    this->openingPercentage = 0;
}

bool DirtyStateTracker::modeSwitchRequested() {
    return this->modeSwitchRequest;
}

void DirtyStateTracker::requestModeSwitch() {
    this->modeSwitchRequest = true;
}

int DirtyStateTracker::getOpeningPercentage() {
    return this->openingPercentage;
}

void DirtyStateTracker::setOpeningPercentage(int openingPercentage) {
    this->openingPercentage = openingPercentage;
}
