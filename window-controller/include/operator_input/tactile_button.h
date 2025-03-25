#ifndef __TACTILE_BUTTON__
#define __TACTILE_BUTTON__

#include "button.h"

class TactileButton: public Button {
    public:
        TactileButton(int pin);
        bool isPressed();

    private:
        int pin;
};

#endif