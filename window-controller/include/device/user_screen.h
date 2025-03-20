#ifndef __USER_SCREEN__
#define __USER_SCREEN__

#include <LiquidCrystal_I2C.h>

class UserScreen {
    public:
        UserScreen();
        void clear();
        void writeRow(int row, String content);

    private:
        LiquidCrystal_I2C *screen;
};

#endif
