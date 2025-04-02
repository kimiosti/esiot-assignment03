package unibo.esiot2024.serial;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import unibo.esiot2024.central.api.CentralController;
import unibo.esiot2024.serial.api.SerialMessHandler;
import unibo.esiot2024.serial.impl.SerialMessHandlerImpl;
import unibo.esiot2024.utils.SystemState;

/**
 * Implementation for the {@link SerialAgent} interface.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            This class must carry a reference to a central controller instance to access the database and make
            Serial line requests effective.
            """
)
public final class SerialAgent implements SerialPortEventListener {

    private static final int BAUDRATE = SerialPort.BAUDRATE_115200;
    private static final int DATA_BITS = SerialPort.DATABITS_8;
    private static final int STOP_BITS = SerialPort.STOPBITS_1;
    private static final int PARITY = SerialPort.PARITY_NONE;
    private static final int CONTROL_MODE_MASK = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
    private static final String LOG_MESSAGE = "Serial line error: ";

    private final CentralController controller;
    private final SerialPort port;
    private final SerialMessHandler messageHandler;
    private String lastMess;

    /**
     * Instantiates a serial agent on the only port attached, or the fallback if more than one port is detected.
     * @param controller the controller for callbacks and database queries.
     * @param portName the serial port to which the agent connects.
    * @throws SerialPortException 
    */
    public SerialAgent(final CentralController controller, final String portName) throws SerialPortException {
        this.controller = controller;
        this.messageHandler = new SerialMessHandlerImpl();
        this.port = new SerialPort(portName);
        this.openPort();
    }

    @Override
    public void serialEvent(final SerialPortEvent event) {
        if (event.isRXCHAR()) {
            try {
                this.lastMess = this.port.readString();
            } catch (final SerialPortException e) {
                this.logError(e);
            }
            new Thread(this::receiveMess).start();
        }
    }

    private void openPort() throws SerialPortException {
        this.port.openPort();
        this.port.setParams(
            BAUDRATE,
            DATA_BITS,
            STOP_BITS,
            PARITY
        );
        this.port.setFlowControlMode(CONTROL_MODE_MASK);
        this.port.addEventListener(this);
        this.port.purgePort(SerialPort.PURGE_RXCLEAR);
    }

    private void receiveMess() {
        final var read = this.messageHandler.parseMess(this.lastMess);
        read.ifPresent(readValues -> {
            if (readValues.modeSwitchRequested()) {
                this.controller.switchOperativeMode();
            }
            this.controller.setOpeningLevel(readValues.openingPercentage());
        });

        final var dbRead = this.controller.getCurrentValues();

        try {
            final var mess = dbRead.isPresent()
                ? this.messageHandler.assembleMess(
                    dbRead.get().measure().temperature(),
                    dbRead.get().state(),
                    dbRead.get().openingPercentage()
                ) : this.messageHandler.assembleMess(0.0f, SystemState.NORMAL, 0);
            port.writeString(mess, "UTF-8");
        } catch (final UnsupportedEncodingException | SerialPortException e) {
            this.logError(e);
        }
    }

    private void logError(final Exception e) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
            Level.SEVERE,
            LOG_MESSAGE + e.getMessage(),
            e
        );
    }

}
