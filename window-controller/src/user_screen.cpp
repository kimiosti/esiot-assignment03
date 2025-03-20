#include "device/user_screen.h"

#define ADDRESS 0x27
#define ROWS 4
#define COLS 16

UserScreen::UserScreen() {
    this->screen = new LiquidCrystal_I2C(ADDRESS, ROWS, COLS);
    this->screen->init();
    this->screen->backlight();
}

void UserScreen::clear() {
    this->screen->clear();
}

void UserScreen::writeRow(int row, String content) {
    this->screen->setCursor(0, row);
    this->screen->print(content);
}