#ifndef __LED__
#define __LED__

class LED {
    private:
        int pin;

    public:
        LED(int pin);
        void turnOn();
        void turnOff();
};

#endif