package unibo.esiot2024.serial;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import unibo.esiot2024.central.api.CentralController;
import unibo.esiot2024.serial.api.SerialMessHandler;
import unibo.esiot2024.serial.impl.SerialMessHandlerImpl;
import unibo.esiot2024.utils.SystemInfo;
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

    private final CentralController controller;
    private final SerialPort port;
    private final SerialMessHandler messageHandler;

    /**
     * Instantiates a serial agent on the only port attached, or the fallback if more than one port is detected.
     * @param controller the controller for callbacks and database queries.
     * @param fallback the fallback port to which the agent connects.
    * @throws SerialPortException 
    */
    public SerialAgent(final CentralController controller, final String fallback) throws SerialPortException {
        this.controller = controller;
        this.messageHandler = new SerialMessHandlerImpl();

        final String portName;
        if (SerialPortList.getPortNames().length != 1) {
            portName = fallback;
        } else {
            portName = SerialPortList.getPortNames()[0];
        }
        this.port = new SerialPort(portName);
        this.openPort();
    }

    @Override
    public void serialEvent(final SerialPortEvent event) {
        Optional<SystemInfo> dbRead = Optional.empty();

        if (event.isRXCHAR()) {
            try {
                final var read = this.messageHandler.parseMess(
                    this.port.readString(event.getEventValue())
                );
                read.ifPresent(readValues -> {
                    if (readValues.modeSwitchRequested()) {
                        this.controller.switchOperativeMode();
                    }
                    this.controller.setOpeningLevel(readValues.openingPercentage());
                });

                dbRead = this.controller.getCurrentValues();
            } catch (final SerialPortException e) {
                if (dbRead.isEmpty()) {
                    dbRead = this.controller.getCurrentValues();
                }
            }

            try {
                port.writeString(
                    dbRead.isPresent()
                        ? this.messageHandler.assembleMess(
                            dbRead.get().measure().temperature(),
                            dbRead.get().state(),
                            dbRead.get().openingPercentage()
                        ) : this.messageHandler.assembleMess(0.0f, SystemState.NORMAL, 0),
                    "UTF-8"
                );
            } catch (UnsupportedEncodingException | SerialPortException e) {
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(
                    Level.SEVERE,
                    "Serial line error: " + e.getMessage(),
                    e
                );
            }
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
    }

}
