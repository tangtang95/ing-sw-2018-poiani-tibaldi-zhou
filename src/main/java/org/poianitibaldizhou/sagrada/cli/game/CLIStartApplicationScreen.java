package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * The firs CLI launched.
 * The class contain the command for starting the game.
 */
public class CLIStartApplicationScreen extends CLIBasicScreen {

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIStartApplicationScreen(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);
        initializeCommands();
    }

    /**
     * Initialize the StartGame's commands.
     */
    @Override
    protected void initializeCommands() {
        Command changeConnectionCommand = new Command(ClientMessage.CHANGE_CONNECTION_MODE,
                ClientMessage.CHANGE_CONNECTION_MODE_HELP);
        changeConnectionCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLIChangeConnectionScreen(connectionManager, screenManager)));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command startGameCommand = new Command(ClientMessage.START_GAME, ClientMessage.START_GAME_HELP);
        startGameCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLISelectGameModeScreen(connectionManager, screenManager)));
        commandMap.put(startGameCommand.getCommandText(), startGameCommand);

        Command quitCommand = new Command(ClientMessage.QUIT_GAME, ClientMessage.QUIT_GAME_HELP);
        quitCommand.setCommandAction(this::quit);
        commandMap.put(quitCommand.getCommandText(), quitCommand);

        Command reconnectCommand = new Command(ClientMessage.RECONNECT, ClientMessage.RECONNECT_HELP);
        reconnectCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLIReconnectToGameScreen(connectionManager,screenManager)));
        commandMap.putIfAbsent(reconnectCommand.getCommandText(), reconnectCommand);
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        pauseCLI();
        CLIBasicScreen.clearScreen();
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.
                buildGraphicLogo().
                buildMessage(ClientMessage.START_GAME_MENU).
                buildGraphicHelp(commandMap).
                buildMessage(ClientMessage.CHOOSE_ACTION).toString(), Level.STANDARD);

        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Quit from the game.
     */
    private void quit() {
        screenManager.popScreen();
        System.exit(0);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStartApplicationScreen has the same commandMap.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStartApplicationScreen)) return false;
        if (!super.equals(o)) return false;
        CLIStartApplicationScreen that = (CLIStartApplicationScreen) o;
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
