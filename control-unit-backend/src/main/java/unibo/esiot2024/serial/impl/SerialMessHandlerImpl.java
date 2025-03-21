package unibo.esiot2024.serial.impl;

import java.util.Optional;

import unibo.esiot2024.serial.api.SerialMessHandler;
import unibo.esiot2024.utils.SystemState;

/**
 * Implementation for the {@link SerialMessHandler} interface.
 */
public final class SerialMessHandlerImpl implements SerialMessHandler {

    private static final String EMPTY = " ";
    private static final String PREFIX = "{ ";
    private static final String POSTFIX = " }";
    private static final String SEPARATOR = ", ";
    private static final String ASSIGNMENT = ":";
    private static final char DELIMITER = '"';


    @Override
    public String assembleMess(final float temperature, final SystemState curState, final int openingPercentage) {
        return PREFIX
            + this.singleField("temperature", temperature)
            + SEPARATOR
            + this.singleField("mode", curState)
            + SEPARATOR
            + this.singleField("openingLevel", openingPercentage)
            + POSTFIX;
    }

    @Override
    public Optional<SerialRead> parseMess(final String message) {
        final var words = message
            .replace(PREFIX, EMPTY)
            .replace(POSTFIX, EMPTY)
            .replace(SEPARATOR, EMPTY)
            .replace(ASSIGNMENT, EMPTY)
            .replace(String.valueOf(DELIMITER), EMPTY)
            .split(EMPTY);

        try {
            return words.length == 4
            ? Optional.of(new SerialRead(Boolean.parseBoolean(words[1]), Integer.parseInt(words[3])))
            : Optional.empty();
        } catch (final NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String singleField(final String name, final Object value) {
        return DELIMITER
            + name
            + DELIMITER
            + ASSIGNMENT
            + EMPTY
            + DELIMITER
            + (
                value instanceof SystemState
                ? (value == SystemState.MANUAL ? "manual" : "automatic")
                : String.valueOf(value)
            )
            + DELIMITER;
    }

}
