#ifndef __DIRTY_STATE_TRACKER__
#define __DIRTY_STATE_TRACKER__

class DirtyStateTracker {
    public:
        DirtyStateTracker();
        bool modeSwitchRequested();
        void requestModeSwitch();
        int getOpeningPercentage();
        void setOpeningPercentage(int openingPercentage);

    private:
        bool modeSwitchRequest;
        int openingPercentage;
};

#endif
