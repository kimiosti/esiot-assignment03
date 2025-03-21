#include <Arduino.h>
#include "communication/communication_task.h"

#define BAUDRATE 9600

const String PREFIX = "{ ";
const String SEPARATOR = ", ";
const String ASSIGNMENT = ": ";
const char DELIMITER = '"';
const String POSTFIX = " }";
const char WHITESPACE = ' ';

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
    String value(PREFIX);
    value.concat("\"modeSwitchRequested\"");
    value.concat(ASSIGNMENT);
    value.concat(DELIMITER);
    value.concat(this->dirty_state_tracker->modeSwitchRequested() ? "true" : "false");
    value.concat(DELIMITER);
    value.concat(SEPARATOR);
    value.concat("\"inputOpeningLevel\"");
    value.concat(ASSIGNMENT);
    value.concat(DELIMITER);
    value.concat(this->dirty_state_tracker->getOpeningPercentage());
    value.concat(DELIMITER);
    value.concat(POSTFIX);
    return value;
}

void CommunicationTask::parseMessage() {
    Serial.readStringUntil(':');
    Serial.readStringUntil(DELIMITER);
    this->state_tracker->setTemperature(Serial.parseFloat());
    Serial.readStringUntil(':');
    Serial.readStringUntil(DELIMITER);
    String read = Serial.readStringUntil(DELIMITER);
    if (read[0] == 'a') {
        this->state_tracker->setMode(AUTOMATIC);
    } else {
        this->state_tracker->setMode(MANUAL);
    }
    Serial.readStringUntil(':');
    Serial.readStringUntil(DELIMITER);
    this->state_tracker->setOpeningPercentage(Serial.parseInt());
    Serial.readString();
}