package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Change connection CLI.
 * You can choose between RMI and SOCKET.
 */
public class CLIChangeConnectionScreen extends CLIBasicScreen {

    /**
     * ChangeConnection commands.
     */
    private static final String GO_BACK = "Go back";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIChangeConnectionScreen(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        initializeCommands();
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
    @Override
    protected void initializeCommands() {
        Command changeConnectionCommand = new Command(
                connectionManager.getNetworkType() == (ConnectionType.RMI) ?
                        ConnectionType.SOCKET.name() : ConnectionType.RMI.name(),
                ClientMessage.CHANGE_CONNECTION);
        changeConnectionCommand.setCommandAction(() -> changeConnection(changeConnectionCommand.getCommandText()));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command goBackCommand = new Command(GO_BACK, ClientMessage.GO_TO_START_MENU);
        goBackCommand.setCommandAction(screenManager::popScreen);
        commandMap.put(goBackCommand.getCommandText(), goBackCommand);
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        CLIBasicScreen.clearScreen();
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.
                buildMessage(ClientMessage.SELECT_CONNECTION_MENU).
                buildMessage(ClientMessage.CURRENT_CONNECTION_MODE + connectionManager.getNetworkType().name()).
                buildGraphicHelp(commandMap).
                buildMessage(ClientMessage.CHANGE_CONNECTION_OR_GO_BACK).
                toString(), Level.STANDARD);

        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Change the connection to the type input.
     *
     * @param type the type of connection to switch.
     */
    private void changeConnection(String type) {
        connectionManager.setPort(ConnectionType.valueOf(type).getPort());
        connectionManager.setNetworkType(ConnectionType.valueOf(type));
        screenManager.popScreen();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIChangeConnectionScreen has the same commandMap.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIChangeConnectionScreen)) return false;
        if (!super.equals(o)) return false;
        CLIChangeConnectionScreen that = (CLIChangeConnectionScreen) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
