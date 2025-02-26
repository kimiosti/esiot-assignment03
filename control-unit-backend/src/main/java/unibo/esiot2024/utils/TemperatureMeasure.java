package unibo.esiot2024.utils;

import java.sql.Date;
import java.sql.Time;

/**
 * Record carrying a temperature measurement.
 * @param temperature the measured temperature.
 * @param date the date in which the temperature was measured.
 * @param time the time in which the temperature was measured.
 */
public record TemperatureMeasure(float temperature, Date date, Time time) {

    /**
     * Instantiates a carrier for a temperature measurement.
     * @param temperature the measured temperature.
     * @param date the date of the measurement.
     * @param time the time of the measurement.
     */
    public TemperatureMeasure(final float temperature, final Date date, final Time time) {
        this.temperature = temperature;
        this.date = Date.valueOf(date.toLocalDate());
        this.time = Time.valueOf(time.toLocalTime());
    }

    /**
     * Getter for the measurement date.
     * @return a {@link Date} object representing the measurement date.
     */
    public Date date() {
        return Date.valueOf(date.toLocalDate());
    }

    /**
     * Getter for the measurement time.
     * @return a {@link Time} object representing the measurement time.
     */
    public Time time() {
        return Time.valueOf(time.toLocalTime());
    }
}
