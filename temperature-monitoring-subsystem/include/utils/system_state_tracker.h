#ifndef __SYSTEM_STATE_TRACKER__
#define __SYSTEM_STATE_TRACKER__

#include "temperature_measure.h"

class SystemStateTracker {
    private:
        int frequency;
        TemperatureMeasure *temperatureMeasure;
        bool online;
        bool subscribed;

    public:
        SystemStateTracker();
        int getcurrentFrequency();
        TemperatureMeasure* getLastMeasure();
        bool isOnline();
        bool isSubscribed();
        void setFrequency(int frequency);
        void recordMeasure(TemperatureMeasure *measure);
        void setOnlineStatus(bool online);
        void setSubscriptionStatus(bool subscription);
};

#endif