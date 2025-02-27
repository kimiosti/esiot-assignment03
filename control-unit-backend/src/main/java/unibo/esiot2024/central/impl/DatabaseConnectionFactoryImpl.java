package unibo.esiot2024.central.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import unibo.esiot2024.central.api.DatabaseConnectionFactory;

/**
 * Implementation for {@link DatabaseConnectionFactory}.
 */
public final class DatabaseConnectionFactoryImpl implements DatabaseConnectionFactory {

    private static final String URL_PREFIX = "jdbc:mysql://";
    private static final List<String> DB_SCRIPT_LOCATORS = List.of(
        "db/create.txt",
        "db/use.txt",
        "db/measurements-table.txt",
        "db/use.txt"
    );

    @Override
    public Connection createConnection(final String url, final String username, final String password, final String dbName) {
        try {
            final var connection = DriverManager.getConnection(URL_PREFIX + url, username, password);
            if (!this.dbExists(connection, dbName)) {
                this.createDatabase(connection);
            }
        } catch (final SQLException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
                Level.SEVERE,
                e.getMessage(),
                e
            );
        }

        try {
            return DriverManager.getConnection(URL_PREFIX + url + "/" + dbName, username, password);
        } catch (final SQLException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
                Level.SEVERE,
                e.getMessage(),
                e
            );
            return null;
        }
    }

    private boolean dbExists(final Connection connection, final String dbName) {
        try (var res = connection.getMetaData().getCatalogs()) {
            while (res.next()) {
                if (res.getString(1).equals(dbName.toLowerCase(Locale.ENGLISH))) {
                    return true;
                }
            }

            return false;
        } catch (final SQLException e) {
            return false;
        }
    }

    private void createDatabase(final Connection connection) {
        for (final var script : DB_SCRIPT_LOCATORS) {
            final StringBuilder query = new StringBuilder();
            try (var reader = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream(script),
                StandardCharsets.UTF_8
            ))) {
                reader.lines().forEach(query::append);
            } catch (final IOException e) {
                this.log(e);
            }

            try (var statement = connection.prepareStatement(query.toString())) {
                statement.execute();
            } catch (final SQLException e) {
                this.log(e);
            }
        }
    }

    private void log(final Exception e) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
            Level.SEVERE,
            e.getMessage(),
            e
        );
    }

}
