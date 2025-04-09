package unibo.esiot2024.central.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private long tooHotStateElapsed;
    private long tooHotLastTS;

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
    public void recordMeasure(final TemperatureMeasure measure) {
        final var curValues = this.getCurrentValues();
        this.accessDatabase(AccessMode.WRITE, Optional.of(new SystemInfo(
            measure,
            this.getStateByTemperature(measure.temperature(), curValues),
            this.getOpeningLevelByTemperature(measure.temperature(), curValues)
        )));
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
        return this.accessDatabase(AccessMode.READ, Optional.empty());
    }

    @Override
    public Optional<Float> getAverageTemperature() {
        return this.getTempFromDBAccess(AccessMode.AVG);
    }

    @Override
    public Optional<Float> getMaxTemperature() {
        return this.getTempFromDBAccess(AccessMode.MAX);
    }

    @Override
    public Optional<Float> getMinTemperature() {
        return this.getTempFromDBAccess(AccessMode.MIN);

    }

    private void recordValues(final float temperature, final SystemState state, final int openingPercentage) {
        this.accessDatabase(AccessMode.WRITE, Optional.of(new SystemInfo(
            new TemperatureMeasure(temperature, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now())),
            state,
            openingPercentage
        )));
    }

    private Optional<Float> getTempFromDBAccess(final AccessMode mode) {
        final var res = this.accessDatabase(mode, Optional.empty());
        return res.isPresent() ? Optional.of(res.get().measure().temperature()) : Optional.empty();
    }

    private synchronized Optional<SystemInfo> accessDatabase(final AccessMode mode, final Optional<SystemInfo> entry) {
        switch (mode) {
            case READ -> {
                return this.handler.getCurrentValues();
            }
            case WRITE -> {
                entry.ifPresent(this.handler::recordNewMeasure);
            }
            case AVG -> {
                final var avg = this.handler.getAverage();
                return avg.isEmpty() ? Optional.empty() : Optional.of(this.generateDummyInfo(avg.get()));
            }
            case MAX -> {
                final var max = this.handler.getMax();
                return max.isEmpty() ? Optional.empty() : Optional.of(this.generateDummyInfo(max.get()));
            }
            case MIN -> {
                final var min = this.handler.getMin();
                return min.isEmpty() ? Optional.empty() : Optional.of(this.generateDummyInfo(min.get()));
            }
        }

        return entry;
    }

    private SystemInfo generateDummyInfo(final float temperature) {
        return new SystemInfo(
            new TemperatureMeasure(temperature, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now())),
            SystemState.NORMAL,
            0
        );
    }

    private SystemState getStateByTemperature(final float temperature, final Optional<SystemInfo> curValues) {
        if (curValues.isPresent() && curValues.get().state().equals(SystemState.MANUAL)) {
            return SystemState.MANUAL;
        } else {
            if (temperature < T1) {
                this.tooHotStateElapsed = 0;
                return SystemState.NORMAL;
            } else if (temperature <= T2) {
                this.tooHotStateElapsed = 0;
                return SystemState.HOT;
            } else {
                final var curTime = System.currentTimeMillis();
                this.tooHotStateElapsed = this.tooHotStateElapsed == 0
                    ? System.currentTimeMillis() - curTime
                    : System.currentTimeMillis() - this.tooHotLastTS;
                this.tooHotLastTS = System.currentTimeMillis();

                return this.tooHotStateElapsed > TOO_HOT_TIME_THRESHOLD ? SystemState.ALARM : SystemState.TOO_HOT;
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

    private enum AccessMode { READ, WRITE, AVG, MAX, MIN }

}
