package unibo.esiot2024;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.Vertx;
import jssc.SerialPortException;
import unibo.esiot2024.central.impl.CentralControllerImpl;
import unibo.esiot2024.mqtt.MQTTAgent;
import unibo.esiot2024.serial.SerialAgent;

/**
 * Launcher class for the backend application.
 */
public final class Application {

    private static final String WRONG_USAGE = """
            Bad arguments! Correct usage: app MySQLusername MySQLpassword serialPortName MQTTbroker
            """;
    private static final String SQL_PROBLEM = """
            Error while connecting to MySQL! Check your credential or your local server status.
            """;
    private static final String SERIAL_PROBLEM = """
            Error while opening the indicated serial port! Check the serial port availability.
            """;

    private Application() { }

    /**
     * Main method for the backend application.
     * @param args eventual command line arguments passed to the application.
     */
    public static void main(final String[] args) {
        try {
            if (args.length == 4) {
                final var controller = new CentralControllerImpl(args[0], args[1]);
                new SerialAgent(controller, args[2]);
                final var vertx = Vertx.vertx();
                vertx.deployVerticle(new MQTTAgent(controller, args[3]));
                // TODO - init HTTPServer
            } else {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, WRONG_USAGE);
            }
        } catch (final SQLException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, SQL_PROBLEM);
        } catch (final SerialPortException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, SERIAL_PROBLEM);
        }
    }
}
