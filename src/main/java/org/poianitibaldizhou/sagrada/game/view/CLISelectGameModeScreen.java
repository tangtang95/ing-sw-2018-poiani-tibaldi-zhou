package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyScreen;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Select game mode menu.
 * You can choose between SINGLE player and MULTI player.
 */
public class CLISelectGameModeScreen extends CLIBasicScreen implements IView {

    /**
     * SelectGameMode commands.
     */
    private static final String SINGLE_PLAYER = "Single player";
    private static final String MULTI_PLAYER = "Multi player";
    private static final String GO_BACK = "Go back";

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
        Command singlePlayerCommand = new Command(SINGLE_PLAYER, "Start in single player mode");
        singlePlayerCommand.setCommandAction(this::newSinglePlayerGame);
        commandMap.put(singlePlayerCommand.getCommandText(), singlePlayerCommand);

        Command multiPlayerCommand = new Command(MULTI_PLAYER, "Start in multi player mode");
        multiPlayerCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLILobbyScreen(connectionManager, screenManager)));
        commandMap.put(multiPlayerCommand.getCommandText(), multiPlayerCommand);

        Command goBackCommand = new Command(GO_BACK, "Go to Start Game Menu");
        goBackCommand.setCommandAction(screenManager::popScreen);
        commandMap.put(goBackCommand.getCommandText(), goBackCommand);
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        pauseCLI();
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("------------------------Select Game Mode---------------------------").
                        buildGraphicHelp(commandMap).
                        buildMessage("Choose the game mode or go to Start Game Menu: ").toString(),
                Level.STANDARD);

        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Creates a new single player game
     */
    private void newSinglePlayerGame() throws RemoteException {
        ConsoleListener.getInstance().stopCommandConsole();
        String gameName = null;
        String token = null;
        String username = null;
        Integer difficulty = null;

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrinterManager.consolePrint("Provide an username: \n", Level.STANDARD);

        while (username == null) {
            try {
                username = r.readLine();
                if (username.equals(""))
                    throw new IllegalArgumentException();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                username = null;
            }
        }

        PrinterManager.consolePrint("Provide a difficulty ranging from 1 to 5: \n", Level.STANDARD);

        while (difficulty == null) {
            try {
                difficulty = Integer.valueOf(r.readLine());
                if (difficulty < 1 || difficulty > 5) {
                    PrinterManager.consolePrint("Difficulty must range from 1 to 5: ", Level.STANDARD);
                    throw new IllegalArgumentException();

                }
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                difficulty = null;
            }
        }

        try {
            String message = connectionManager.getGameController().createSinglePlayer(clientCreateMessage.
                    createUsernameMessage(username).createValueMessage(difficulty).buildMessage());
            gameName = clientGetMessage.getGameName(message);
            token = clientGetMessage.getToken(message);
        } catch (IOException e) {
            PrinterManager.consolePrint("Error in creating single player game", Level.STANDARD);
            return;
        }

        ConsoleListener.getInstance().wakeUpCommandConsole();
        screenManager.replaceScreen(new CLISetupGameScreen(connectionManager, screenManager, gameName, new UserWrapper(username), token));
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


}
