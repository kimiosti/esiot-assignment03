package unibo.esiot2024.central.db_access.api;

import java.util.Optional;

import unibo.esiot2024.utils.SystemInfo;

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
     * @return a {@link SystemInfo} instance representing the state of the system at the time of the highest temperature
     * measure in the last minute, or an empty {@link Optional} if no value has been recorded in the last minute.
     */
    Optional<SystemInfo> getMax();

    /**
     * Returns the lowest temperature measure in the last minute.
     * @return a {@link SystemInfo} instance representing the state of the system at the time of the lowest temperature
     * measure in the last minute, or an empty {@link Optional} if no value has been recorded in the last minute.
     */
    Optional<SystemInfo> getMin();
}
