package unibo.esiot2024.central.db_access.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Enumeration representing all possible database queries.
 */
@SuppressFBWarnings(
    value = {
        "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
        "OBL_UNSATISFIED_OBLIGATION"
    },
    justification = """
            The class generates SQL from hard-coded strings, which are therefore constants (false positive) and
            delegates the resource closing obligation to the caller. This because the caller might need to work on the
            resource, and could not do it if it were closed by this class.
            """
)
public enum Queries {

    /**
     * Query to retrieve a single database entry.
     */
    GETTER_QUERY(
        """
                SELECT *
                FROM measurements
                ORDER BY measureID DESC
                LIMIT 1
                """
    ),

    /**
     * Query to insert an entry in the database.
     */
    SETTER_QUERY(
        """
                INSERT INTO measurements (temperature, measureDate, measureTime, state, openingLevel)
                VALUES (?, ?, ?, ?, ?)
                """
    ),

    /**
     * Query to get the average temperature value of the last minute.
     */
    AVG_QUERY(
        """
                SELECT AVG(temperature) AS avgTemp
                FROM measurements
                WHERE TIME_TO_SEC(TIMEDIFF(CURIME(), measureTime)) < 60
                """
    ),

    /**
     * Query to get the maximum temperature value in the last minute.
     */
    MAX_QUERY(
        """
                SELECT MAX(temperature) AS maxTemp
                FROM measurements
                WHERE TIME_TO_SEC(TIMEDIFF(CURTIME(), m.measureTime)) < 60
                """
    ),

    /**
     * Query to get the minimum temperature value in the last minute.
     */
    MIN_QUERY(
        """
                SELECT MIN(temperature) AS minTemp
                FROM measurements
                WHERE TIME_TO_SEC(TIMEDIFF(CURTIME(), m.measureTime)) < 60
                """
    );

    private final String sql;

    Queries(final String sql) {
        this.sql = sql;
    }

    /**
     * Converts the enumeration label into a statement.
     * @param connection the connection used to generate the statement.
     * @param params the (eventual) parameters for the statement.
     * @return a {@link PreparedStatement} generated from the enumeration label's SQL text,
     * eventually including the given parameters. 
     * @throws SQLException if the statement can't be prepared correctly.
     */
    public PreparedStatement toStatement(final Connection connection, final Object... params) throws SQLException {
        final var statement = connection.prepareStatement(this.sql);
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
        return statement;
    }
}
