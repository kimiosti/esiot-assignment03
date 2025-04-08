#include <Arduino.h>
#include "communication/communication_task.h"

#define BAUDRATE 115200
#define INCOMING_MESS_LENGTH 14

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
    this->incoming_message = new String();

    Serial.begin(BAUDRATE);
}

void CommunicationTask::step(long sched_period) {
    this->step_count++;
    if (this->step_count * sched_period >= this->period) {
        this->step_count = 0;
        Serial.println(this->assembleMessage());
        while (Serial.available()) {
            this->incoming_message->concat((char) Serial.read());
            if (this->incoming_message->length() >= INCOMING_MESS_LENGTH) {
                this->parseMessage();
            }
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
    float temp = this->incoming_message->substring(1, 6).toFloat();
    bool isManual = this->incoming_message->charAt(8) == '1';
    int opening = this->incoming_message->substring(11).toInt();

    this->state_tracker->setTemperature(temp);
    this->state_tracker->setMode(isManual ? MANUAL : AUTOMATIC);
    this->state_tracker->setOpeningPercentage(opening);

    delete this->incoming_message;
    this->incoming_message = new String();
}