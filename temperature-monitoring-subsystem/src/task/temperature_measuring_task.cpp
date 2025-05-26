#include <time.h>
#include "task/temperature_measuring_task.h"

#define DEFAULT_PERIOD 500
#define BASE_YEAR 1900
#define MONTH_CORRECTOR 1
#define DATE_SEPARATOR '-'

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
    struct tm timestruct;

    getLocalTime(&timestruct);

    char date[10];
    sprintf(date, "%d-%02d-%02d", timestruct.tm_year + BASE_YEAR, timestruct.tm_mon + MONTH_CORRECTOR, timestruct.tm_mday);

    char time[8];
    sprintf(time, "%02d:%02d:%02d", timestruct.tm_hour, timestruct.tm_min, timestruct.tm_sec);

    TemperatureMeasure measure(tempMeasure, String(date), String(time));

    if (xSemaphoreTake(sharedDataMutex, this->period / portTICK_PERIOD_MS)) {
        this->period = this->stateTracker->getcurrentFrequency();
        this->stateTracker->recordMeasure(&measure);
        xSemaphoreGive(sharedDataMutex);
    }
}