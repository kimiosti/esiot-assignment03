#include "utils/system_state_tracker.h"

#define DEFAULT_FREQUENCY 1000
#define DEFAULT_MEASURE 0.0

SystemStateTracker::SystemStateTracker() {
    this->frequency = DEFAULT_FREQUENCY;
    this->temperatureMeasure = DEFAULT_MEASURE;
    this->online = false;
    this->subscribed = false;
}

unsigned long SystemStateTracker::getcurrentFrequency() {
    return this->frequency;
}

float SystemStateTracker::getLastMeasure() {
    return this->temperatureMeasure;
}

bool SystemStateTracker::isOnline() {
    return this->online;
}

bool SystemStateTracker::isSubscribed() {
    return this->subscribed;
}

void SystemStateTracker::setFrequency(unsigned long frequency) {
    this->frequency = frequency;
}

void SystemStateTracker::recordMeasure(float measure) {
    this->temperatureMeasure = measure;
}

void SystemStateTracker::setOnlineStatus(bool online) {
    this->online = online;
}

void SystemStateTracker::setSubscriptionStatus(bool subscribed) {
    this->subscribed = subscribed;
}
