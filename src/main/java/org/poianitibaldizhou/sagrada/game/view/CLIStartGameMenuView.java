package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class CLIStartGameMenuView extends CLIMenuView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String CHANGE_CONNECTION_MODE = "Change connection mode";
    private static final String START_GAME = "Star game";
    private static final String QUIT = "Quit";

    public CLIStartGameMenuView(NetworkManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super(networkManager, screenManager, bufferManager);
        initializeCommands();
    }

    private void initializeCommands() {
        Command changeConnectionCommand = new Command(CHANGE_CONNECTION_MODE, "Go to Change connection menu");
        changeConnectionCommand.setCommandAction(() ->
            screenManager.pushScreen(new CLIChangeConnectionMenuView(networkManager, screenManager, bufferManager)));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command startGameCommand = new Command(START_GAME, "Go to Game mode menu");
        startGameCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLISelectGameModeMenuView(networkManager, screenManager, bufferManager)));
        commandMap.put(startGameCommand.getCommandText(), startGameCommand);

        Command quitCommand = new Command(QUIT, "Quit game");
        quitCommand.setCommandAction(this::quit);
        commandMap.put(quitCommand.getCommandText(), quitCommand);
    }



    @Override
    public void run() {
        BuildGraphic buildGraphic = new BuildGraphic();

        bufferManager.consolePrint(buildGraphic.
                buildGraphicLogo().
                buildMessage("-------------------------Start Game Menu---------------------------").
                buildGraphicHelp(commandMap).
                buildMessage("Choose action: ").toString(),Level.LOW);

        try {
            bufferManager.consolePrint("Choose action: ", Level.LOW);
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        } catch (NullPointerException e) {
            //...
        }
    }

    private void quit(){
        screenManager.popScreen();
        System.exit(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStartGameMenuView)) return false;
        if (!super.equals(o)) return false;
        CLIStartGameMenuView that = (CLIStartGameMenuView) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
