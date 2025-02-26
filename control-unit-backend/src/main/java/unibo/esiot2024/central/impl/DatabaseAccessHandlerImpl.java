package unibo.esiot2024.central.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import unibo.esiot2024.central.api.DatabaseAccessHandler;
import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.SystemState;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Implementation for the {@link DatabaseAccessHandler} interface.
 */
public final class DatabaseAccessHandlerImpl implements DatabaseAccessHandler {

    private static final String CONNECTION_URL = "localhost:3306";
    private static final String GETTER_QUERY = """
            SELECT *
            FROM measurements
            ORDER BY measureID DESC
            LIMIT 1
            """;
    private static final String SETTER_QUERY = """
            INSERT INTO measurements(temperature, measureDate, measureTime, state, openingLevel)
            VALUES ?, ?, ?, ?, ?
            """;

    private final Connection connection;
    private final Map<String, SystemState> stateNameToState;

    /**
     * Instantiates a new database access handler for the system.
     * @param username the username for the database connection.
     * @param password the password for the database connection.
     * @throws SQLException 
     */
    public DatabaseAccessHandlerImpl(final String username, final String password) throws SQLException {
        // TODO - optionally handle DB schema creation and selection
        this.connection = DriverManager.getConnection(
            CONNECTION_URL,
            username,
            password
        );
        this.stateNameToState = Map.of(
            "normal", SystemState.NORMAL,
            "hot", SystemState.HOT,
            "too hot", SystemState.TOO_HOT,
            "alarm", SystemState.ALARM,
            "manual", SystemState.MANUAL
        );
    }

    @Override
    public synchronized void recordNewMeasure(final Optional<TemperatureMeasure> measure, final Optional<SystemState> state,
            final Optional<Integer> openingPercentage) {
        final var curValues = this.getCurrentValues();

        if (curValues.isPresent()) {
            final var statement = this.createParametrizedStatement(
                SETTER_QUERY,
                measure.isPresent() ? measure.get().temperature() : curValues.get().measure().temperature(),
                measure.isPresent() ? measure.get().date() : curValues.get().measure().date(),
                measure.isPresent() ? measure.get().time() : curValues.get().measure().time(),
                state.isPresent() ? state.get() : curValues.get().state(),
                openingPercentage.isPresent() ? openingPercentage.get() : curValues.get().openingPercentage()
            );
            if (statement != null) {
                try (statement) {
                    statement.execute();
                } catch (final SQLException e) {
                    Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
                        Level.SEVERE,
                        e.getSQLState(),
                        e
                    );
                }
            }
        }
    }

    @Override
    public synchronized Optional<SystemInfo> getCurrentValues() {
        try (var statement = this.connection.prepareStatement(GETTER_QUERY)) {
            final var resSet = statement.executeQuery();

            if (resSet.next()) {
                return Optional.of(new SystemInfo(
                    new TemperatureMeasure(
                        resSet.getFloat("temperature"),
                        resSet.getDate("measureDate"),
                        resSet.getTime("measureTime")
                    ),
                    this.getStateFromString(resSet.getString("state")),
                    resSet.getInt("openingLevel")
                ));
            } else {
                return Optional.empty();
            }
        } catch (final SQLException e) {
            return Optional.empty();
        }
    }

    private SystemState getStateFromString(final String stateName) {
        return this.stateNameToState.containsKey(stateName) ? this.stateNameToState.get(stateName) : SystemState.MANUAL;
    }

    private PreparedStatement createParametrizedStatement(final String query, final Object... params) {
        try (var statement = this.connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            return statement;
        } catch (final SQLException e) {
            return null;
        }
    }
}
