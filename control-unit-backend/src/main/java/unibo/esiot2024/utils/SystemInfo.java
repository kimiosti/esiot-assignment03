package unibo.esiot2024.utils;

/**
 * Record carrying all system's informations at a given time.
 * @param measure the last - and therefore currently valid - temperature measurement.
 * @param state the current state of the system.
 * @param openingPercentage the current opening percentage of the window.
 */
public record SystemInfo(
    TemperatureMeasure measure,
    SystemState state,
    int openingPercentage
) {

    /**
     * Instantiates a system info carrier.
     * @param measure the last measure.
     * @param state the current system state.
     * @param openingPercentage the current opening percentage of the window.
     */
    public SystemInfo(final TemperatureMeasure measure, final SystemState state, final int openingPercentage) {
        this.measure = new TemperatureMeasure(measure.temperature(), measure.timestamp());
        this.state = state;
        this.openingPercentage = openingPercentage;
    }
}
