package unibo.esiot2024.serial.impl;

/**
 * A record carrying a serial message read values.
 * @param modeSwitchRequested represents if the user has requested an operative mode switch.
 * @param openingPercentage represents the requested opening percentage.
 */
public record SerialRead(boolean modeSwitchRequested, int openingPercentage) { }
