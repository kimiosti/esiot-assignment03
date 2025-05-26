#include <Arduino.h>
#include "task/temperature_measuring_task.h"

#define DEFAULT_PERIOD 500

TemperatureMeasuringTask::TemperatureMeasuringTask(
    int sensorPin, SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex
) {
    this->period = DEFAULT_PERIOD;
    this->sensor = new TemperatureSensor(sensorPin);
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;
}

void TemperatureMeasuringTask::run(void *params) {
    for (;;) {
        unsigned long start = millis();
        this->update();
        unsigned long elapsed = millis() - start;
        if (this->period - elapsed > 0) {
            delay(this->period - elapsed);
        }
    }
}

void TemperatureMeasuringTask::update() {
    float tempMeasure = this->sensor->getTemperature();

    if (xSemaphoreTake(sharedDataMutex, this->period / portTICK_PERIOD_MS)) {
        this->period = this->stateTracker->getcurrentFrequency();
        this->stateTracker->recordMeasure(tempMeasure);
        xSemaphoreGive(sharedDataMutex);
    }
}