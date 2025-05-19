#include "utils/system_state_tracker.h"

#define DEFAULT_FREQUENCY 1000

SystemStateTracker::SystemStateTracker() {
    this->frequency = DEFAULT_FREQUENCY;
    this->temperatureMeasure = new TemperatureMeasure(0.0, String("0000-00-00"), String("00:00:00"));
    this->online = false;
    this->subscribed = false;
}

int SystemStateTracker::getcurrentFrequency() {
    return this->frequency;
}

TemperatureMeasure* SystemStateTracker::getLastMeasure() {
    return this->temperatureMeasure;
}

bool SystemStateTracker::isOnline() {
    return this->online;
}

bool SystemStateTracker::isSubscribed() {
    return this->subscribed;
}

void SystemStateTracker::setFrequency(int frequency) {
    this->frequency = frequency;
}

void SystemStateTracker::recordMeasure(TemperatureMeasure *measure) {
    delete this->temperatureMeasure;
    this->temperatureMeasure = measure;
}

void SystemStateTracker::setOnlineStatus(bool online) {
    this->online = online;
}

void SystemStateTracker::setSubscriptionStatus(bool subscribed) {
    this->subscribed = subscribed;
}
