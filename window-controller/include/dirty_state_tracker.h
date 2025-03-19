#ifndef __DIRTY_STATE_TRACKER__
#define __DIRTY_STATE_TRACKER__

class DirtyStateTracker {
    public:
        DirtyStateTracker();
        bool modeSwitchRequested();
        void requestModeSwitch();
        int getOpeningPercentage();
        void setOpeningPercentage(int opening_percentage);

    private:
        bool mode_switch_request;
        int opening_percentage;
};

#endif
