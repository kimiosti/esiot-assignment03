package unibo.esiot2024;

import java.sql.SQLException;

import jssc.SerialPortException;
import unibo.esiot2024.central.impl.CentralControllerImpl;
import unibo.esiot2024.serial.SerialAgent;

/**
 * Launcher class for the backend application.
 */
public final class Application {

    private Application() { }

    /**
     * Main method for the backend application.
     * @param args eventual command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        new GUI("Insert controller setup configuration");
    }


    /**
     * Launches the backend application.
     * @param dbUser the username for the database connection.
     * @param dbPass the password for the database connetcion.
     * @param serialPort the default port for the serial connection.
     * @param broker the default MQTT broker.
     * @throws SQLException
    */
    public static void launch(final String dbUser, final String dbPass, final String serialPort, final String broker) {
        try {
            final var controller = new CentralControllerImpl(dbUser, dbPass);
            new SerialAgent(controller, serialPort);
        } catch (final SQLException e) {
            new GUI("Database connection error", "check your credentials and the state of the MySQL server");
        } catch (final SerialPortException e) {
            new GUI("Error while connecting to Serial Line", "check the serial port state");
        }
    }
}
