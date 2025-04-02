#include <Arduino.h>
#include "communication/communication_task.h"

#define BAUDRATE 115200

String threeDigitRepresentation(int n) {
    String rep("");
    for (int i = 100; i > 0; i /= 10) {
        rep.concat(n / i);
        n -= (n / i) * i;
    }
    return rep;
}

CommunicationTask::CommunicationTask(long period, DirtyStateTracker *dirty_state_tracker, StateTracker *state_tracker) {
    this->step_count = 0;
    this->period = period;
    this->dirty_state_tracker = dirty_state_tracker;
    this->state_tracker = state_tracker;

    Serial.begin(BAUDRATE);
}

void CommunicationTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period >= this->period) {
        this->step_count = 0;
        Serial.println(this->assembleMessage());
        if (Serial.available()) {
            this->parseMessage();
        }
    }
}

String CommunicationTask::assembleMessage() {
    String message("");
    message.concat(this->dirty_state_tracker->modeSwitchRequested() ? "t" : "f");
    message.concat(' ');
    message.concat(threeDigitRepresentation(this->dirty_state_tracker->getOpeningPercentage()));

    return message;
}

void CommunicationTask::parseMessage() {

}