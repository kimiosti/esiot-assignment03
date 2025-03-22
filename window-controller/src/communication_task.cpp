#include <Arduino.h>
#include "communication/communication_task.h"

#define BAUDRATE 9600

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
    message.concat(this->dirty_state_tracker->modeSwitchRequested() ? "true" : "false");
    message.concat(' ');
    message.concat(this->dirty_state_tracker->getOpeningPercentage());

    return message;
}

void CommunicationTask::parseMessage() {
    float temp = Serial.available() ? Serial.parseFloat() : this->state_tracker->getTemperature();
    Mode mode = Serial.available() ? (Serial.parseInt() == 0 ? MANUAL : AUTOMATIC) : this->state_tracker->getMode();
    int opening = Serial.available() ? Serial.parseInt() : this->state_tracker->getOpeningPercentage();
    if (Serial.available()) Serial.readString();

    this->state_tracker->setTemperature(temp);
    this->state_tracker->setMode(mode);
    this->state_tracker->setOpeningPercentage(opening);
}