package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

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
    protected final transient ConnectionManager networkManager;

    /**
     * Manager for handler the changed of the screen.
     */
    protected final transient ScreenManager screenManager;

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIBasicScreen(ConnectionManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super();
        this.networkManager = networkManager;
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
     * @param o the other object to compare.
     * @return true if the CLIChangeConnectionScreen has the same commandMap, networkManager and screenManager.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIBasicScreen)) return false;
        if (!super.equals(o)) return false;
        CLIBasicScreen that = (CLIBasicScreen) o;
        return Objects.equals(commandMap, that.commandMap) &&
                Objects.equals(networkManager, that.networkManager) &&
                Objects.equals(screenManager, that.screenManager);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap, networkManager, screenManager);
    }
}
