package unibo.esiot2024.central.db_access.api;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for a database connection factory.
 */
public interface DatabaseConnectionFactory {

    /**
     * Safely creates a database connection, ensuring the database exists.
     * @param url the URL of the desired database connection.
     * @param username the username to be used in the connection.
     * @param password the password to be used in the connection.
     * @param dbName the name of the database.
     * @return a {@link Connection} to operate on the given database.
     */
    Connection createConnection(String url, String username, String password, String dbName) throws SQLException;
}
