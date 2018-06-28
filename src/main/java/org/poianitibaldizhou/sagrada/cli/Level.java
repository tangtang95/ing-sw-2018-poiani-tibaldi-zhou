package org.poianitibaldizhou.sagrada.cli;

/**
 * Level enum for defining the importance of the message to print.
 * There are three types of level:
 * - standard: used to communicate simple information.
 * - error: used to communicate error or particular exceptions.
 * - information: used to communicate game information.
 */
public enum Level {
    STANDARD,
    INFORMATION,
    ERROR
}
