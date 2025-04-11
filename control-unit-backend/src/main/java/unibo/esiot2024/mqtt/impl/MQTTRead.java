package unibo.esiot2024.mqtt.impl;

import java.sql.Date;
import java.sql.Time;

/**
 * MQTT message content carrier.
 * @param temp the temperature indicated in the message.
 * @param date the date of the measurement indicated in the message.
 * @param time the time of the measurement indicated in the message.
 */
public record MQTTRead(float temp, Date date, Time time) {

    /**
     * Instantiates an MQTT message content carrier.
     * @param temp the measured temperature.
     * @param date the date of the measurement.
     * @param time the time of the measurement.
     */
    public MQTTRead(final float temp, final Date date, final Time time) {
        this.temp = temp;
        this.date = Date.valueOf(date.toLocalDate());
        this.time = Time.valueOf(time.toLocalTime());
    }

    @Override
    public Date date() {
        return Date.valueOf(this.date.toLocalDate());
    }

    @Override
    public Time time() {
        return Time.valueOf(this.time.toLocalTime());
    }
}
