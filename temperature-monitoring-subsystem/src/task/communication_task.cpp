#include "task/communication_task.h"
#include "utils/temperature_measure.h"

#define DEFAULT_PERIOD 200
#define SEND_TOPIC "unibo/esiot2024/temp-monitor/temp-monitor"
#define RECEIVE_TOPIC "unibo/esiot2024/temp-monitor/backend"

CommunicationTask::CommunicationTask(
    SystemStateTracker *stateTracker, SemaphoreHandle_t sharedDataMutex, PubSubClient *mqttClient
) {
    this->stateTracker = stateTracker;
    this->sharedDataMutex = sharedDataMutex;
    this->mqttClient = mqttClient;

    while(!xSemaphoreTake(sharedDataMutex, DEFAULT_PERIOD / portTICK_PERIOD_MS)) { }
    this->period = stateTracker->getcurrentFrequency();
    xSemaphoreGive(sharedDataMutex);
}

void CommunicationTask::run(void *params) {
    for(;;) {
        unsigned long start = millis();
        this->update();
        unsigned long elapsed = millis() - start;
        if (this->period - elapsed > 0) {
            delay(this->period - elapsed);
        }
    }
}

void CommunicationTask::update() {
    this->mqttClient->loop();

    if (xSemaphoreTake(this->sharedDataMutex, this->period / portTICK_PERIOD_MS)) {
        TemperatureMeasure *measure = this->stateTracker->getLastMeasure();
        this->period = this->stateTracker->getcurrentFrequency();
        xSemaphoreGive(this->sharedDataMutex);

        String msg;
        msg.concat("{ \"temp\": ");
        msg.concat(String(measure->getTemperature(), 1U));
        msg.concat(", \"date\": ");
        msg.concat(measure->getDate());
        msg.concat(", \"time\": ");
        msg.concat(measure->getTime());
        msg.concat(" }");

        this->mqttClient->publish(SEND_TOPIC, msg.c_str());
    }
}

void CommunicationTask::onReceive(char *topic, byte *payload, unsigned int length) {
    if (String(topic).compareTo(String(RECEIVE_TOPIC)) == 0) {
        unsigned long newPeriod = String((char *)payload).substring(15, 18).toInt();

        if (xSemaphoreTake(this->sharedDataMutex, newPeriod / portTICK_PERIOD_MS)) {
            this->stateTracker->setFrequency(newPeriod);
            xSemaphoreGive(this->sharedDataMutex);
        }
    }
}