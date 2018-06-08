package org.poianitibaldizhou.sagrada.cli;

import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * ConsoleListener is a singleton class that rule the input stream from the console command.
 *
 */
public class ConsoleListener {

    /**
     * private instance of ConsoleListener (singleton pattern).
    */
    private static ConsoleListener ourInstance;

    /**
     * map of the commands for controlling that the input is correct and for lunching the command.
     */
    private Map<String, Command> commandMap = new HashMap<>();

    /**
     * Object used to handle thread concurrency.
     */
    private boolean needToPause;

    /**
     * Object use for locking the CommandConsole.
     */
    private final Object lock = new Object();

    /**
     * constructor of ConsoleListener.
     */
    private ConsoleListener() {
        CommandConsole commandConsole = new CommandConsole();
        commandConsole.start();
    }

    /**
     * Static method for getting the instance of ConsoleListener (singleton pattern).
     *
     * @return the instance of the singleton class.
     */
    public static synchronized ConsoleListener getInstance() {
        if (ourInstance == null)
            ourInstance = new ConsoleListener();
        return ourInstance;
    }

    /**
     * Pause keyboard listener thread.
     */
    public void stopCommandConsole() { needToPause = true; }

    /**
     * Wake up keyboard listener thread.
     */
    public void wakeUpCommandConsole() {
        synchronized (lock) {
            needToPause = false;
            lock.notifyAll();
        }
    }

    /**
     * Set the commandMap.
     *
     * @param commandMap to set.
     */
    public void setCommandMap(Map<String, Command> commandMap) {
        this.commandMap = commandMap;
    }

    /**
     * Read a number from keyboard stream.
     *
     * @param maxInt max number that is could to insert.
     * @return a number read - 1.
     */
    public int readPositionNumber(int maxInt) {
        stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int key = 0;
        do {
            try {
                String read = r.readLine();
                key = Integer.parseInt(read);
                if (!(key > 0 && key <= maxInt))
                    throw new CommandNotFoundException();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (NumberFormatException e) {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.ERROR);
            } catch (CommandNotFoundException e) {
                PrinterManager.consolePrint(BuildGraphic.COMMAND_NOT_FOUND, Level.ERROR);
                key = 0;
            }
        } while (key < 1);
        wakeUpCommandConsole();
        return key - 1;
    }

    /**
     * Read a number from keyboard stream.
     *
     * @param maxInt max number that is could to insert.
     * @return a number read.
     */
    public int readValue(int maxInt){
        return readPositionNumber(maxInt) + 1;
    }


    /**
     * Thread for reading the command from CLI.
     * Keyboard input handler.
     */
    private class CommandConsole extends Thread {

        private BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        private BuildGraphic buildGraphic = new BuildGraphic();
        boolean exit = true;
        int key;

        @Override
        public void run() {
            while (exit) {
                try {
                    consoleReady();
                    String read = console.readLine();
                    if (read.equals("help"))
                        PrinterManager.consolePrint(buildGraphic.buildGraphicHelp(commandMap).toString(), Level.STANDARD);
                    else {
                        key = Integer.parseInt(read);
                        if (key > 0 && key <= commandMap.keySet().size()) {
                            commandMap.get(commandMap.keySet().toArray()[key - 1].toString()).executeCommand();
                        } else
                            throw new CommandNotFoundException();
                    }
                } catch (IOException e) {
                    PrinterManager.consolePrint(this.getClass().getSimpleName() +
                            BuildGraphic.ERROR_READING, Level.ERROR);
                    break;
                } catch (NumberFormatException e) {
                    PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.ERROR);
                } catch (CommandNotFoundException e) {
                    PrinterManager.consolePrint(BuildGraphic.COMMAND_NOT_FOUND, Level.ERROR);
                }
            }
        }

        /**
         * Put the thread in pause, lock on the lock object.
         */
        void pausePoint() {
            synchronized (lock) {
                while (needToPause) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        PrinterManager.consolePrint(this.getClass().getSimpleName() + ": Error while pausing.\n",
                                Level.ERROR);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        /**
         * @throws IOException is launched by the ready function (BufferReader class).
         */
        void consoleReady() throws IOException {
            while (!console.ready())
                pausePoint();
        }

    }

}
