package unibo.esiot2024;

import java.sql.SQLException;

import jssc.SerialPortException;
import unibo.esiot2024.central.impl.CentralControllerImpl;
import unibo.esiot2024.serial.SerialAgent;

/**
 * Launcher class for the backend application.
 */
public final class Application {

    private static final String FALLBACK_SERIAL_PORT = "COM4";

    private Application() { }

    /**
     * Main method for the backend application.
     * @param args eventual command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        new GUI("Insert credentials to access MySQL database");
    }


    /**
     * Launches the backend application.
     * @param dbUser the username for the database connection.
     * @param dbPass the password for the database connetcion.
     * @throws SQLException
    */
    public static void launch(final String dbUser, final String dbPass) {
        try {
            final var controller = new CentralControllerImpl(dbUser, dbPass);
            new SerialAgent(controller, FALLBACK_SERIAL_PORT);
        } catch (final SQLException e) {
            new GUI("Database connection error", "check your credentials and the state of the MySQL server");
        } catch (final SerialPortException e) {
            new GUI("Error while connecting to Serial Line", "Insert database credentials to log back in");
        }
    }
}
