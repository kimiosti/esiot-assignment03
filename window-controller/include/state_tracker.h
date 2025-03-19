#ifndef __STATE_TRACKER__
#define __STATE_TRACKER__

enum Mode {
    MANUAL,
    AUTOMATIC
};

class StateTracker {
    public:
        StateTracker();
        float getTemperature();
        void setTemperature(float temperature);
        int getOpeningPercentage();
        void setOpeningPercentage(int opening_percentage);
        Mode getMode();
        void setMode(Mode mode);

    private:
        float temperature;
        int opening_percentage;
        Mode mode;
};

#endif