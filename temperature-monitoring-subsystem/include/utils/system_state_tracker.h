#ifndef __SYSTEM_STATE_TRACKER__
#define __SYSTEM_STATE_TRACKER__

class SystemStateTracker {
    private:
        unsigned long frequency;
        float temperatureMeasure;
        bool online;
        bool subscribed;

    public:
        SystemStateTracker();
        unsigned long getcurrentFrequency();
        float getLastMeasure();
        bool isOnline();
        bool isSubscribed();
        void setFrequency(unsigned long frequency);
        void recordMeasure(float measure);
        void setOnlineStatus(bool online);
        void setSubscriptionStatus(bool subscription);
};

#endif