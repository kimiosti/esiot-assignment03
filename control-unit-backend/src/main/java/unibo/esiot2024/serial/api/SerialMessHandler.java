package unibo.esiot2024.serial.api;

import java.util.Optional;

import unibo.esiot2024.serial.impl.SerialRead;
import unibo.esiot2024.utils.SystemState;

/**
 * Handler for serial line messages.
 */
public interface SerialMessHandler {

    /**
     * Assembles an outgoing message from the relevant system values.
     * @param temperature the last valid measure.
     * @param curState the current state of the system.
     * @param openingPercentage the current window opening percentage.
     * @return a string containing the message in JSON format, supported by the Serial line communication.
     */
    String assembleMess(float temperature, SystemState curState, int openingPercentage);

    /**
     * Parses a message received on the serial line.
     * @param message the received message.
     * @return an {@link Optional} containing a {@link SerialRead} representing the values in the message or empty
     * if the read wasn't valid.
     */
    Optional<SerialRead> parseMess(String message);
}
