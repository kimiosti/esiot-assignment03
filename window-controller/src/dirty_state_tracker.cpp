#include "dirty_state_tracker.h"

DirtyStateTracker::DirtyStateTracker() {
    this->mode_switch_request = false;
    this->opening_percentage = 0;
}

bool DirtyStateTracker::modeSwitchRequested() {
    if (this->mode_switch_request) {
        this->mode_switch_request = false;
        return true;
    } else {
        return false;
    }
}

void DirtyStateTracker::requestModeSwitch() {
    this->mode_switch_request = !this->mode_switch_request;
}

int DirtyStateTracker::getOpeningPercentage() {
    return this->opening_percentage;
}

void DirtyStateTracker::setOpeningPercentage(int opening_percentage) {
    this->opening_percentage = opening_percentage;
}
