package unibo.esiot2024.central.db_access.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import unibo.esiot2024.central.db_access.api.DatabaseAccessHandler;
import unibo.esiot2024.utils.SystemInfo;
import unibo.esiot2024.utils.SystemState;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Implementation for the {@link DatabaseAccessHandler} interface.
 */
public final class DatabaseAccessHandlerImpl implements DatabaseAccessHandler {

    private static final String CONNECTION_URL = "localhost:3306";
    private static final String DATABASE_NAME = "SmartTemperatureMonitor";

    private final Connection connection;
    private final Map<String, SystemState> stateNameToState;

    /**
     * Instantiates a new database access handler for the system.
     * @param username the username for the database connection.
     * @param password the password for the database connection.
     * @throws SQLException 
     */
    public DatabaseAccessHandlerImpl(final String username, final String password) throws SQLException {
        this.connection = new DatabaseConnectionFactoryImpl().createConnection(
            CONNECTION_URL,
            username,
            password,
            DATABASE_NAME
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
    public void recordNewMeasure(final SystemInfo entry) {
                try (var statement = Queries.SETTER_QUERY.toStatement(
                    this.connection,
                    entry.measure().temperature(),
                    entry.measure().date(),
                    entry.measure().time(),
                    entry.state().getState(),
                    entry.openingPercentage()
                )) {
                    statement.execute();
                } catch (final SQLException e) {
                    this.log(e);
                }
    }

    @Override
    public Optional<SystemInfo> getCurrentValues() {
        try (var statement = Queries.GETTER_QUERY.toStatement(this.connection)) {
            final var resSet = statement.executeQuery();

            return resSet.next() ? Optional.of(this.getInfoFromResultSet(resSet)) : Optional.empty();
        } catch (final SQLException e) {
            this.log(e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Float> getAverage() {
        try (var statement = Queries.AVG_QUERY.toStatement(this.connection)) {
            final var resSet = statement.executeQuery();

            return resSet.next() ? Optional.of(resSet.getFloat("avgTemp")) : Optional.empty();
        } catch (final SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Float> getMax() {
        try (var statement = Queries.MAX_QUERY.toStatement(this.connection)) {
            final var resSet = statement.executeQuery();

            return resSet.next() ? Optional.of(resSet.getFloat("maxTemp")) : Optional.empty();
        } catch (final SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Float> getMin() {
        try (var statement = Queries.MIN_QUERY.toStatement(this.connection)) {
            final var resSet = statement.executeQuery();

            return resSet.next() ? Optional.of(resSet.getFloat("minTemp")) : Optional.empty();
        } catch (final SQLException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TemperatureMeasure> getLastMeasures() {
        try (var statement = Queries.LAST_MEASURES_QUERY.toStatement(this.connection)) {
            final var resSet = statement.executeQuery();

            final var res = new ArrayList<TemperatureMeasure>();
            while(resSet.next()) {
                res.add(new TemperatureMeasure(
                    resSet.getFloat("temperature"),
                    resSet.getDate("measureDate"),
                    resSet.getTime("measureTime")
                ));
            }
            return res;
        } catch (final SQLException e) {
            return List.of();
        }
    }

    private SystemInfo getInfoFromResultSet(final ResultSet resSet) throws SQLException {
        return new SystemInfo(
            new TemperatureMeasure(
                resSet.getFloat("temperature"),
                resSet.getDate("measureDate"),
                resSet.getTime("measureTime")
            ),
            this.getStateFromString(resSet.getString("state")),
            resSet.getInt("openingLevel")
        );
    }

    private SystemState getStateFromString(final String stateName) {
        return this.stateNameToState.containsKey(stateName) ? this.stateNameToState.get(stateName) : SystemState.MANUAL;
    }

    private void log(final Exception e) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
            Level.SEVERE,
            e.getMessage(),
            e
        );
    }

}
