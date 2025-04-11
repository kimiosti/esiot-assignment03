package unibo.esiot2024.mqtt.api;

import unibo.esiot2024.utils.TemperatureMeasure;

/**
 * MQTT message handler.
 */
public interface MQTTMessHandler {

    /**
     * Parses a message as received from the MQTT broker.
     * @param mess the message to be parsed.
     * @return a {@link MQTTRead} instance carrying the data.
     */
    TemperatureMeasure parseMess(String mess);

    /**
     * Assembles a message to be published via MQTT.
     * @param frequency the frequency to be sent via MQTT.
     * @return a {@link String} representing the correctly formatted message.
     */
    String assembleMess(float frequency);
}
