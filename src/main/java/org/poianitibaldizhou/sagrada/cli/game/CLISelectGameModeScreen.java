package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.cli.lobby.CLILobbyScreen;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * Select game mode menu.
 * You can choose between SINGLE player and MULTI player.
 */
public class CLISelectGameModeScreen extends CLIBasicScreen implements IView {

    private final transient ClientCreateMessage clientCreateMessage;
    private final transient ClientGetMessage clientGetMessage;

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLISelectGameModeScreen(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();

        initializeCommands();
    }

    /**
     * Initialize the SelectGameMode's commands.
     */
    @Override
    protected void initializeCommands() {
        Command singlePlayerCommand = new Command(ClientMessage.SINGLE_PLAYER, ClientMessage.SINGLE_PLAYER_HELP);
        singlePlayerCommand.setCommandAction(this::newSinglePlayerGame);
        commandMap.put(singlePlayerCommand.getCommandText(), singlePlayerCommand);

        Command multiPlayerCommand = new Command(ClientMessage.MULTI_PLAYER, ClientMessage.MULTI_PLAYER_HELP);
        multiPlayerCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLILobbyScreen(connectionManager, screenManager)));
        commandMap.put(multiPlayerCommand.getCommandText(), multiPlayerCommand);

        Command goBackCommand = new Command(ClientMessage.GO_BACK, ClientMessage.GO_BACK_HELP);
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

        PrinterManager.consolePrint(buildGraphic.
                        buildMessage(ClientMessage.SELECT_GAME_MODE).
                        buildGraphicHelp(commandMap).
                        buildMessage(ClientMessage.CHOOSE_GAME_MODE).toString(),
                Level.STANDARD);

        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Creates a new single player game
     */
    private void newSinglePlayerGame() {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.stopCommandConsole();

        String username = null;

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrinterManager.consolePrint(ClientMessage.PROVIDE_AN_USERNAME, Level.STANDARD);

        try {
            while (username == null) {
                username = r.readLine();
                if (username.equals(""))
                    username = null;
            }

            PrinterManager.consolePrint(ClientMessage.PROVIDE_DIFFICULTY, Level.STANDARD);
            int difficulty = consoleListener.readValue(5);

            String message = connectionManager.getGameController().createSinglePlayer(clientCreateMessage.
                    createUsernameMessage(username).createValueMessage(difficulty).buildMessage());
            String gameName = clientGetMessage.getGameName(message);
            String token = clientGetMessage.getToken(message);

            consoleListener.wakeUpCommandConsole();
            screenManager.replaceScreen(new CLISetupGameScreen(connectionManager, screenManager,
                    new CLISinglePlayerScreen(connectionManager, screenManager, gameName, token),
                    new UserWrapper(username)));

        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.ERROR_READING, Level.ERROR);
        } catch (TimeoutException e) {
            /*NEVER THROWN HERE*/
        }
        consoleListener.wakeUpCommandConsole();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIChangeConnectionScreen has the same commandMap.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISelectGameModeScreen)) return false;
        if (!super.equals(o)) return false;
        CLISelectGameModeScreen that = (CLISelectGameModeScreen) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint(ack + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint(err + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ping(){
        // DO NOTHING
    }


}
