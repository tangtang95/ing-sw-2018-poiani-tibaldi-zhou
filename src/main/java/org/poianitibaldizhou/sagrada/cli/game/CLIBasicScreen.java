package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Basic CLI.
 */
public abstract class CLIBasicScreen extends UnicastRemoteObject implements IScreen {

    /**
     * Map of the command for the CLI.
     */
    protected final transient Map<String, Command> commandMap = new HashMap<>();

    /**
     * Network manager for connecting with the server.
     */
    protected final transient ConnectionManager connectionManager;

    /**
     * Manager for handler the changed of the screen.
     */
    protected final transient ScreenManager screenManager;

    /**
     * Time to sleep.
     */
    private static final int TIME_SLEEP = 100;

    /**
     * constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIBasicScreen(ConnectionManager connectionManager, ScreenManager screenManager)
            throws RemoteException {
        super();
        this.connectionManager = connectionManager;
        this.screenManager = screenManager;
    }

    /**
     * Initialize the CLI commands.
     */
    protected abstract void initializeCommands();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void startCLI();

    /**
     * This method pose the CLI in pause for TIME_SLEEP millisecond, for
     * attending the notify.
     */
    void pauseCLI() {
        try {
            Thread.sleep(TIME_SLEEP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Clear the screen.
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            //  Handle any exceptions.
        }
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIChangeConnectionScreen has the same commandMap, connectionManager and screenManager.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIBasicScreen)) return false;
        if (!super.equals(o)) return false;
        CLIBasicScreen that = (CLIBasicScreen) o;
        return Objects.equals(commandMap, that.commandMap) &&
                Objects.equals(connectionManager, that.connectionManager) &&
                Objects.equals(screenManager, that.screenManager);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap, connectionManager, screenManager);
    }
}
