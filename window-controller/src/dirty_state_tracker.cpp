#include "dirty_state_tracker.h"

DirtyStateTracker::DirtyStateTracker() {
    this->mode_switch_request = 0;
    this->opening_percentage = 0;
}

bool DirtyStateTracker::modeSwitchRequested() {
    return this->mode_switch_request;
}

void DirtyStateTracker::requestModeSwitch() {
    this->mode_switch_request = true;
}

int DirtyStateTracker::getOpeningPercentage() {
    return this->opening_percentage;
}

void DirtyStateTracker::setOpeningPercentage(int opening_percentage) {
    this->opening_percentage = opening_percentage;
}
