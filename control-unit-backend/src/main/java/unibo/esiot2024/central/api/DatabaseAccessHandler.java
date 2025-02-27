package unibo.esiot2024.central.api;

import java.util.Optional;

import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.SystemState;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Interface for the system database access handler.
 */
public interface DatabaseAccessHandler {

    /**
     * Records a new measure in the database.
     * @param measure the temperature, date and time of measurement.
     * @param state the current state of the system.
     * @param openingPercentage the current opening percentage of the window.
     */
    void recordNewMeasure(TemperatureMeasure measure, SystemState state, int openingPercentage);

    /**
     * Reads the last valid measure from the database.
     * @return a {@link SystemInfo} instance representing the current state of all values tracked by the system.
     */
    Optional<SystemInfo> getCurrentValues();
}
