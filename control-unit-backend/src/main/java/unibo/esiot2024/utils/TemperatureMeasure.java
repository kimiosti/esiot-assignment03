package unibo.esiot2024.utils;

import java.util.Date;

/**
 * Record carrying a temperature measurement.
 * @param temperature the measured temperature.
 * @param timestamp the timestamp in which the measure was performed.
 */
public record TemperatureMeasure(float temperature, Date timestamp) {

    /**
     * Instantiates a temperature measure carrier.
     * @param temperature the value measured by the sensor.
     * @param timestamp the time at which the measure occurred.
     */
    public TemperatureMeasure(final float temperature, final Date timestamp) {
        this.temperature = temperature;
        this.timestamp = Date.from(timestamp.toInstant());
    }

    /**
     * Getter for the measure's timestamp.
     * @return a {@link Date} object representing the moment in which the measure was captured.
     */
    public Date timestamp() {
        return Date.from(this.timestamp.toInstant());
    }
}
