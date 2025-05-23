package unibo.esiot2024.central.db_access.api;

import java.util.List;
import java.util.Optional;

import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Interface for the system database access handler.
 */
public interface DatabaseAccessHandler {

    /**
     * Records a new measure in the database.
     * @param entry the values to be recorded in the database.
     */
    void recordNewMeasure(SystemInfo entry);

    /**
     * Reads the last valid measure from the database.
     * @return a {@link SystemInfo} instance representing the current state of all values tracked by the system,
     * or an empty {@link Optional} if no value is yet in the database.
     */
    Optional<SystemInfo> getCurrentValues();

    /**
     * Returns the average temperature measure in the last minute.
     * @return a {@link Float} instance with the value of the average temperature, or an empty {@link Optional} if no
     * measure was recorded in the last minute.
     */
    Optional<Float> getAverage();

    /**
     * Returns the highest temperature measure in the last minute.
     * @return a {@link Float} instance with the value of the lowest temperature of the last minute,
     * or an empty {@link Optional} if no value has been recorded in the last minute.
     */
    Optional<Float> getMax();

    /**
     * Returns the lowest temperature measure in the last minute.
     * @return a {@link Float} instance with the value of the highest temperature of the last minute,
     * or an empty {@link Optional} if no value has been recorded in the last minute.
     */
    Optional<Float> getMin();

    /**
     * Returns a list containing all the measures recorded in the last minute.
     * @return a {@link List} of {@link TemperatureMeasure} containing all the measurements recorded in
     * the last minute, or an empty {@link List} if no measure was found in the last minute.
     */
    List<TemperatureMeasure> getLastMeasures();
}
