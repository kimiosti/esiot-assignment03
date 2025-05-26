package unibo.esiot2024.central.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import unibo.esiot2024.central.api.CentralController;
import unibo.esiot2024.central.db_access.api.DatabaseAccessHandler;
import unibo.esiot2024.central.db_access.impl.DatabaseAccessHandlerImpl;
import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.SystemState;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Implementation for the {@link CentralController} implementation.
 */
public final class CentralControllerImpl implements CentralController {

    private static final long MODE_SWITCH_THRESHOLD = 250;
    private static final float T1 = 30.0f;
    private static final float T2 = 35.0f;
    private static final long TOO_HOT_TIME_THRESHOLD = 5000;
    private static final int OPENING_TOLERANCE = 5;

    private final DatabaseAccessHandler handler;
    private long lastModeSwitchTS;
    private long tooHotElapsed;
    private long tooHotLast;

    /**
     * Initialises an instance for the central controller of the application.
     * @param dbUser the username for the database connection.
     * @param dbPass the password for the database connection.
     * @throws SQLException if the database connection can't be established.
     */
    public CentralControllerImpl(final String dbUser, final String dbPass) throws SQLException {
        this.handler = new DatabaseAccessHandlerImpl(dbUser, dbPass);
        this.lastModeSwitchTS = System.currentTimeMillis();
    }

    @Override
    public void switchOperativeMode() {
        final var curValues = this.getCurrentValues();
        if (curValues.isPresent() && System.currentTimeMillis() - this.lastModeSwitchTS > MODE_SWITCH_THRESHOLD) {
            this.lastModeSwitchTS = System.currentTimeMillis();
            this.recordValues(
                curValues.get().measure().temperature(),
                curValues.get().state().equals(SystemState.MANUAL) ? SystemState.NORMAL : SystemState.MANUAL,
                curValues.get().openingPercentage()
            );
        }
    }

    @Override
    public void setOpeningLevel(final int openingPercentage) {
        final var curValues = this.getCurrentValues();
        if (curValues.isPresent()
            && curValues.get().state().equals(SystemState.MANUAL)
            && Math.abs(curValues.get().openingPercentage() - openingPercentage) > OPENING_TOLERANCE
        ) {
            this.recordValues(
                curValues.get().measure().temperature(),
                curValues.get().state(),
                openingPercentage
            );
        }
    }

    @Override
    public SystemState recordMeasure(final TemperatureMeasure measure) {
        final var curValues = this.getCurrentValues();
        this.handler.recordNewMeasure(new SystemInfo(
            measure,
            this.getStateByTemperature(measure.temperature(), curValues),
            this.getOpeningLevelByTemperature(measure.temperature(), curValues)
        ));
        return this.getStateByTemperature(measure.temperature(), curValues);
    }

    @Override
    public void restoreIssue() {
        final var curValues = this.getCurrentValues();
        if (curValues.isPresent() && curValues.get().state().equals(SystemState.ALARM)) {
            this.recordValues(
                curValues.get().measure().temperature(),
                SystemState.NORMAL,
                curValues.get().openingPercentage()
            );
        }
    }

    @Override
    public Optional<SystemInfo> getCurrentValues() {
        return this.handler.getCurrentValues();
    }

    @Override
    public Optional<Float> getAverageTemperature() {
        return this.handler.getAverage();
    }

    @Override
    public Optional<Float> getMaxTemperature() {
        return this.handler.getMax();
    }

    @Override
    public Optional<Float> getMinTemperature() {
        return this.handler.getMin();

    }

    @Override
    public List<TemperatureMeasure> getLastMeasures() {
        return this.handler.getLastMeasures();
    }

    private void recordValues(final float temperature, final SystemState state, final int openingPercentage) {
        this.handler.recordNewMeasure(new SystemInfo(
            new TemperatureMeasure(temperature, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now())),
            state,
            openingPercentage
        ));
    }

    private SystemState getStateByTemperature(final float temperature, final Optional<SystemInfo> curValues) {
        if (curValues.isPresent() && curValues.get().state().equals(SystemState.MANUAL)) {
            return SystemState.MANUAL;
        } else {
            if (temperature < T1) {
                return SystemState.NORMAL;
            } else if (temperature > T2) {
                if (curValues.isEmpty()) {
                    this.tooHotElapsed = 0;
                    this.tooHotLast = System.currentTimeMillis();
                    return SystemState.TOO_HOT;
                } else {
                    if (curValues.get().state().equals(SystemState.ALARM)) {
                        return SystemState.ALARM;
                    } else if (curValues.get().state().equals(SystemState.TOO_HOT)) {
                        this.tooHotElapsed += System.currentTimeMillis() - this.tooHotLast;
                        this.tooHotLast = System.currentTimeMillis();
                        return this.tooHotElapsed > TOO_HOT_TIME_THRESHOLD ? SystemState.ALARM : SystemState.TOO_HOT;
                    } else {
                        this.tooHotElapsed = 0;
                        this.tooHotLast = System.currentTimeMillis();
                        return SystemState.TOO_HOT;
                    }
                }
            } else {
                return SystemState.HOT;
            }
        }
    }

    private int getOpeningLevelByTemperature(final float temperature, final Optional<SystemInfo> curValues) {
        if (curValues.isPresent() && curValues.get().state().equals(SystemState.MANUAL)) {
            return curValues.get().openingPercentage();
        } else {
            return temperature < T1 ? 0
                : temperature > T2 ? 100
                : (int) ((temperature - T1) / (T2 - T1) * 100);
        }
    }

}
