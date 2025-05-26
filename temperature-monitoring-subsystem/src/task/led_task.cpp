#include <Arduino.h>
#include "task/led_task.h"

LEDTask::LEDTask(
    long period, int redPin, int greenPin, SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex
) {
    this->period = period;
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;

    this->green = new LED(greenPin);
    this->red = new LED(redPin);
}

void LEDTask::run(void *params) {
    for (;;) {
        unsigned long start = millis();
        this->update();
        unsigned long elapsed = millis() - start;
        if (period > elapsed) {
           delay(period - elapsed);
        }
    }
}

void LEDTask::update() {
    bool networkOk;

    if (xSemaphoreTake(sharedDataMutex, pdMS_TO_TICKS(this->period))) {
        networkOk = stateTracker->isOnline() && stateTracker->isSubscribed();
        xSemaphoreGive(sharedDataMutex);

        if (networkOk) {
            red->turnOff();
            green->turnOn();
        } else {
            green->turnOff();
            red->turnOn();
        }
    }
}