package org.poianitibaldizhou.sagrada.cli;

/**
 * Class used to print messages on the standard output in order to debug.
 * Three type of debug message:
 * standard: used to communicate simple information.
 * error: used to communicate error or particular exceptions.
 * information: used to communicate game information.
 */
public final class PrinterManager {

    /**
     * constructor
     */
    private PrinterManager() {}

    /**
     * print a message
     *
     * @param message to print
     * @param level of the message
     */
    public static synchronized void consolePrint(String message, Level level) {
        if (level == Level.STANDARD)
            if (!message.equals("") && message.substring(0, 1).equals("\n"))
                System.out.print("\b\b\b" + message + ">> ");
            else
                System.out.print("\r" + message + ">> ");
        else
        if (!message.equals("") && message.substring(0, 1).equals("\n"))
            System.out.print("\b\b\b" + level.name() + ": " + message + ">> ");
        else
            System.out.print("\r" + level.name() + ": " + message + ">> ");
    }
}
