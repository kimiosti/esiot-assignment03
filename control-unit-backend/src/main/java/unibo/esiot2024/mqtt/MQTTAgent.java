package unibo.esiot2024.mqtt;

import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.messages.MqttPublishMessage;
import unibo.esiot2024.central.api.CentralController;
import unibo.esiot2024.mqtt.api.MQTTMessHandler;
import unibo.esiot2024.mqtt.impl.MQTTMessHandlerImpl;
import unibo.esiot2024.utils.SystemState;

/**
 * MQTT Agent of the control unit.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            This class must carry a reference to a central controller instance to access the database and make
            MQTT requests effective.
            """
)
public final class MQTTAgent extends AbstractVerticle {

    private static final int BROKER_PORT = 1883;
    private static final String BASE_TOPIC = "unibo/esiot2024/temp-monitor/";
    private static final String SEND_TOPIC = BASE_TOPIC + "backend";
    private static final String RECEIVE_TOPIC = BASE_TOPIC + "temp-monitor";
    private static final int RECEIVE_QOS = 2;
    private static final boolean SEND_IS_DUP = false;
    private static final boolean SEND_IS_RETAIN = false;
    private static final int F1 = 500;
    private static final int F2 = 300;
    private static final int F3 = 200;
    private static final int FM = F1;

    private final CentralController controller;
    private final String broker;
    private final MQTTMessHandler handler;
    private final Map<SystemState, Integer> stateToFrequency;

    /**
     * Instantiates an MQTT agent.
     * @param controller the central controller for callbacks.
     * @param broker the URL of the broker to subscribe to.
     */
    public MQTTAgent(final CentralController controller, final String broker) {
        this.controller = controller;
        this.broker = broker;
        this.handler = new MQTTMessHandlerImpl();
        this.stateToFrequency = Map.of(
            SystemState.NORMAL, F1,
            SystemState.HOT, F2,
            SystemState.TOO_HOT, F3,
            SystemState.ALARM, F3,
            SystemState.MANUAL, FM
        );
    }

    @Override
    public void start() {
        final var client = MqttClient.create(vertx);

        client.connect(
            BROKER_PORT,
            broker,
            c -> this.setup(client)
        );
    }

    private void setup(final MqttClient client) {
        client.publishHandler(mess -> this.handleMess(mess, client)).subscribe(RECEIVE_TOPIC, RECEIVE_QOS);
    }

    private void handleMess(final MqttPublishMessage mess, final MqttClient client) {
        final var read = this.handler.parseMess(mess.payload().toString());
        final var state = this.controller.recordMeasure(read);

        client.publish(
            SEND_TOPIC,
            Buffer.buffer(this.handler.assembleMess(this.stateToFrequency.get(state))),
            MqttQoS.AT_LEAST_ONCE,
            SEND_IS_DUP,
            SEND_IS_RETAIN
        );
    }
}
