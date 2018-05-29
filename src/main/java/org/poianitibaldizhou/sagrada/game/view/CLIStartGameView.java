package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStartGameView extends CLIBasicView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String CHANGE_CONNECTION_MODE = "Change connection mode";
    private static final String START_GAME = "Start game";
    private static final String QUIT = "Quit";

    public CLIStartGameView(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        initializeCommands();
    }

    private void initializeCommands() {
        Command changeConnectionCommand = new Command(CHANGE_CONNECTION_MODE, "Go to Change connection menu");
        changeConnectionCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLIChangeConnectionView(networkManager, screenManager)));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command startGameCommand = new Command(START_GAME, "Go to Game mode menu");
        startGameCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLISelectGameModeView(networkManager, screenManager)));
        commandMap.put(startGameCommand.getCommandText(), startGameCommand);

        Command quitCommand = new Command(QUIT, "Quit game");
        quitCommand.setCommandAction(this::quit);
        commandMap.put(quitCommand.getCommandText(), quitCommand);
    }


    @Override
    public void run() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                buildGraphicLogo().
                buildMessage("-------------------------Start Game Menu---------------------------").
                buildGraphicHelp(commandMap).
                buildMessage("Choose action: ").toString(), Level.STANDARD);

        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
    }

    private void quit() {
        screenManager.popScreen();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStartGameView)) return false;
        if (!super.equals(o)) return false;
        CLIStartGameView that = (CLIStartGameView) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
