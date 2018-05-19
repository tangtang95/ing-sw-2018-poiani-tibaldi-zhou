package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CLISelectGameModeMenuView extends CLIMenuView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String SINGLE_PLAYER = "Single player";
    private static final String MULTI_PLAYER = "Multi player";
    private static final String GO_BACK = "Go back";

    CLISelectGameModeMenuView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);
        initializeCommands();
    }

    private void initializeCommands() {
        Command singlePlayerCommand = new Command(SINGLE_PLAYER, "Start in single player mode");
        singlePlayerCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLIGameView(networkManager, screenManager)));
        commandMap.put(singlePlayerCommand.getCommandText(), singlePlayerCommand);

        Command multiPlayerCommand = new Command(MULTI_PLAYER, "Start in multi player mode");
        multiPlayerCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLILobbyView(networkManager,screenManager)));
        commandMap.put(multiPlayerCommand.getCommandText(), multiPlayerCommand);

        Command goBackCommand = new Command(GO_BACK, "Go to Start Game Menu");
        goBackCommand.setCommandAction(screenManager::popScreen);
        commandMap.put(goBackCommand.getCommandText(), goBackCommand);
    }
    @Override
    public void run() {
        bufferManager.formatPrint("------------------------Select Game Mode---------------------------",
                Level.LOW);
        help(commandMap);
        try {
            bufferManager.formatPrint("Choose the game mode or go to Start Game Menu: ", Level.LOW);
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLISelectGameModeMenuView)) return false;
        if (!super.equals(o)) return false;
        CLISelectGameModeMenuView that = (CLISelectGameModeMenuView) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
