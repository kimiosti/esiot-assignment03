#ifndef __TEMPERATURE_SENSOR__
#define __TEMPERATURE_SENSOR__

class TemperatureSensor {
    private:
        int pin;

    public:
        TemperatureSensor(int pin);
        float getTemperature();
};

#endif