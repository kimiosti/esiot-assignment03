#include <Arduino.h>
#include "scheduler/scheduler.h"
#include "scheduler/coop_r_r_scheduler.h"
#include "scheduler/task.h"
//#include "window_controller/window_controlling_task.h"
#include "operator_output/operator_output_task.h"
//#include "operator_input/operator_input_task.h"
#include "communication/communication_task.h"
#include "dirty_state_tracker.h"
#include "state_tracker.h"

Scheduler *scheduler;
Task *communication_task;
//Task *window_controlling_task;
Task *operator_output_task;
//Task *operator_input_task;
DirtyStateTracker *dirty_state_tracker;
StateTracker *state_tracker;

void setup() {
  // put your setup code here, to run once:
  dirty_state_tracker = new DirtyStateTracker();
  state_tracker = new StateTracker();
  scheduler = new CoopRRScheduler(50);

  //window_controlling_task = new WindowControllingTask(200, dirty_state_tracker, state_tracker);
  operator_output_task = new OperatorOutputTask(100, state_tracker);
  //operator_input_task = new OperatorInputTask(50, dirty_state_tracker, state_tracker);
  communication_task = new CommunicationTask(200, dirty_state_tracker, state_tracker);

  //scheduler->bind(window_controlling_task);
  scheduler->bind(operator_output_task);
  //scheduler->bind(operator_input_task);
  scheduler->bind(communication_task);
}

void loop() {
  // put your main code here, to run repeatedly:
  scheduler->schedule();
}
