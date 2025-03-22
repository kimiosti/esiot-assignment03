package unibo.esiot2024.serial.impl;

import java.util.Optional;

import unibo.esiot2024.serial.api.SerialMessHandler;
import unibo.esiot2024.utils.SystemState;

/**
 * Implementation for the {@link SerialMessHandler} interface.
 */
public final class SerialMessHandlerImpl implements SerialMessHandler {

    private static final String EMPTY = " ";


    @Override
    public String assembleMess(final float temperature, final SystemState curState, final int openingPercentage) {
        return "T"
            + String.valueOf(temperature)
            + " M"
            + (curState.equals(SystemState.MANUAL) ? "0" : "1")
            + " O"
            + String.valueOf(openingPercentage);
    }

    @Override
    public Optional<SerialRead> parseMess(final String message) {
        final var words = message.split(EMPTY);
        if (words.length == 2) {
            try {
                return Optional.of(new SerialRead(
                    Boolean.valueOf(words[0]),
                    Integer.valueOf(words[1]).intValue()
                ));
            } catch (final NumberFormatException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
