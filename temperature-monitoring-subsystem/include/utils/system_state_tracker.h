#ifndef __SYSTEM_STATE_TRACKER__
#define __SYSTEM_STATE_TRACKER__

#include "temperature_measure.h"

class SystemStateTracker {
    private:
        unsigned long frequency;
        TemperatureMeasure *temperatureMeasure;
        bool online;
        bool subscribed;

    public:
        SystemStateTracker();
        unsigned long getcurrentFrequency();
        TemperatureMeasure* getLastMeasure();
        bool isOnline();
        bool isSubscribed();
        void setFrequency(unsigned long frequency);
        void recordMeasure(TemperatureMeasure *measure);
        void setOnlineStatus(bool online);
        void setSubscriptionStatus(bool subscription);
};

#endif