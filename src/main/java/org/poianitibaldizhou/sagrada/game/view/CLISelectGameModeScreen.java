package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyScreen;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Select game mode menu.
 * You can choose between SINGLE player and MULTI player.
 */
public class CLISelectGameModeScreen extends CLIBasicScreen {

    /**
     * SelectGameMode commands.
     */
    private static final String SINGLE_PLAYER = "Single player";
    private static final String MULTI_PLAYER = "Multi player";
    private static final String GO_BACK = "Go back";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLISelectGameModeScreen(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        initializeCommands();
    }

    /**
     * Initialize the SelectGameMode's commands.
     */
    @Override
    protected void initializeCommands() {
        Command singlePlayerCommand = new Command(SINGLE_PLAYER, "Start in single player mode");
        singlePlayerCommand.setCommandAction(() ->
                new CLIStateScreen(networkManager, screenManager, "Empire", new User("Tang", "master")));
        commandMap.put(singlePlayerCommand.getCommandText(), singlePlayerCommand);

        Command multiPlayerCommand = new Command(MULTI_PLAYER, "Start in multi player mode");
        multiPlayerCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLILobbyScreen(networkManager,screenManager)));
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
}
