#ifndef __TEMPERATURE_MEASURE__
#define __TEMPERATURE_MEASURE__

#include <Arduino.h>

#define DATE_LENGTH 10
#define TIME_LENGTH 8

class TemperatureMeasure {
    private:
        float temperature;
        String date;
        String time;
    
    public:
        TemperatureMeasure(float temperature, String date, String time);
        float getTemperature();
        String getDate();
        String getTime();
};

#endif