#include <Arduino.h>
#include "utils/system_state_tracker.h"

SystemStateTracker *stateTracker;

SemaphoreHandle_t stateTrackerMutex;

void setup() {
  // put your setup code here, to run once:
  stateTracker = new SystemStateTracker();

  stateTrackerMutex = xSemaphoreCreateMutex();
}

void loop() {
  // put your main code here, to run repeatedly:
}