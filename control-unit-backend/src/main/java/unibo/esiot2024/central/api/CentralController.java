package unibo.esiot2024.central.api;

import java.util.Optional;

import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Interface for the core controller component of the backend application.
 */
public interface CentralController {

    /**
     * Requests a switch between manual and automatic operative mode.
     */
    void switchOperativeMode();

    /**
     * Requests the operning level to be set to a certain amount.
     * @param openingPercentage the window's requested opening percentage.
     */
    void setOpeningLevel(int openingPercentage);

    /**
     * Requests a certain measure to be recorded.
     * @param measure the measure to be recorded.
     */
    void recordMeasure(TemperatureMeasure measure);

    /**
     * Requests the system to restore its functions.
     */
    void restoreIssue();

    /**
     * Requests the current state of the system.
     * @return a {@link SystemInfo} instance containing the last valid measure.
     */
    Optional<SystemInfo> getCurrentValues();

    /**
     * Getter for the average temperature measurement of the last minute.
     * @return a {@link Float} instance containing the average value,
     * or an empty {@link Optional} if no values have been recorded in the last minute.
     */
    Optional<Float> getAverageTemperature();

    /**
     * Getter for the highest temperature measured in the last minute.
     * @return a {@link Float} instance containing the highest measured value,
     * or an empty {@link Optional} if no values have been recorded in the last minute.
     */
    Optional<Float> getMaxTemperature();

    /**
     * Getter for the lowest temperature measured in the last minute.
     * @return a {@link Float} instance containing the highest measured value,
     * or an empty {@link Optional} if no values have been recorded in the last minute.
     */
    Optional<Float> getMinTemperature();
}
