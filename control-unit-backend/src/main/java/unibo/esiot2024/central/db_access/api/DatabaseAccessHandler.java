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
     * @return a {@link SystemInfo} instance representing the current state of all values tracked by the system.
     */
    Optional<SystemInfo> getCurrentValues();
}
