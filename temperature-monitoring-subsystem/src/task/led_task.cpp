#include "task/led_task.h"

#define RED_LED_PIN 5
#define GREEN_LED_PIN 4

LEDTask::LEDTask(
    long period, SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex
) {
    this->period = period;
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;

    this->green = new LED(GREEN_LED_PIN);
    this->red = new LED(RED_LED_PIN);
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