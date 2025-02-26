package unibo.esiot2024.utils;

/**
 * Enumeration listing all possible system states.
 */
public enum SystemState {
    /**
     * State representing that the temperature is under control.
     */
    NORMAL("normal"),

    /**
     * State representing a higher than expected temperature value.
     */
    HOT("hot"),

    /**
     * State representing an out of control temperature value.
     */
    TOO_HOT("too hot"),

    /**
     * State representing a critical situation, occurring when the system is too long for too much time.
     */
    ALARM("alarm"),

    /**
     * State representing that the system is currently manually operated.
     */
    MANUAL("manual");

    private final String state;

    SystemState(final String state) {
        this.state = state;
    }

    /**
     * Method to retrieve the current system state.
     * @return a state representation as a {@link String}.
     */
    public String getState() {
        return this.state;
    }
}
