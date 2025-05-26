package unibo.esiot2024.mqtt.impl;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import unibo.esiot2024.mqtt.api.MQTTMessHandler;
import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * Implementation for the {@link MQTTMessHandler} interface.
 */
public final class MQTTMessHandlerImpl implements MQTTMessHandler {

    private static final int TEMP_INDEX = 1;
    private static final String SPLIT_REGEX = "[{}, ]";
    private static final String PREFIX = "{ ";
    private static final String POSTFIX = " }";
    private static final String FREQUENCY_LABEL = "\"frequency\": ";

    @Override
    public TemperatureMeasure parseMess(final String mess) {
        final var words = new ArrayList<String>(List.of(mess.split(SPLIT_REGEX)));
        words.removeIf(String::isEmpty);
        return new TemperatureMeasure(
            Float.parseFloat(words.get(TEMP_INDEX)),
            Date.valueOf(LocalDate.now()),
            Time.valueOf(LocalTime.now())
        );
    }

    @Override
    public String assembleMess(final int frequency) {
        return PREFIX
            + FREQUENCY_LABEL
            + frequency
            + POSTFIX;
    }

}
