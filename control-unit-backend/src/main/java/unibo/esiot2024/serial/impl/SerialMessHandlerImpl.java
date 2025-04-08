package unibo.esiot2024.serial.impl;

import java.util.Locale;
import java.util.Optional;

import unibo.esiot2024.serial.api.SerialMessHandler;
import unibo.esiot2024.utils.SystemState;

/**
 * Implementation for the {@link SerialMessHandler} interface.
 */
public final class SerialMessHandlerImpl implements SerialMessHandler {

    private static final int INCOMING_MESS_LENGTH = 7;
    private static final int MODE_SWITCH_BEGIN = 0;
    private static final int MODE_SWITCH_END = 1;
    private static final int OPENING_PERCENTAGE_BEGIN = 2;
    private static final int OPENING_PERCENTAGE_END = 5;
    private static final String TEMPERATURE_FORMAT = "%02.2f";
    private static final String OPENING_FORMAT = "%03d";

    @Override
    public String assembleMess(final float temperature, final SystemState curState, final int openingPercentage) {
        return "T"
            + String.format(TEMPERATURE_FORMAT, temperature).replace(',', '.')
            + " M"
            + (curState.equals(SystemState.MANUAL) ? "1" : "0")
            + " O"
            + String.format(OPENING_FORMAT, openingPercentage, Locale.US);
    }

    @Override
    public Optional<SerialRead> parseMess(final String message) {
        if (message != null && message.length() == INCOMING_MESS_LENGTH) {
            return Optional.of(new SerialRead(
                "t".equals(message.substring(MODE_SWITCH_BEGIN, MODE_SWITCH_END)),
                Integer.parseInt(message.substring(OPENING_PERCENTAGE_BEGIN, OPENING_PERCENTAGE_END))
            ));
        } else {
            return Optional.empty();
        }
    }

}
