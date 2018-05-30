package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * The firs CLI launched.
 * The class contain the command for starting the game.
 */
public class CLIStartGameScreen extends CLIBasicScreen {

    /**
     * StartGame commands.
     */
    private static final String CHANGE_CONNECTION_MODE = "Change connection mode";
    private static final String START_GAME = "Start game";
    private static final String QUIT = "Quit";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIStartGameScreen(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        initializeCommands();
    }

    /**
     * Initialize the StartGame's commands.
     */
    @Override
    protected void initializeCommands() {
        Command changeConnectionCommand = new Command(CHANGE_CONNECTION_MODE, "Go to Change connection menu");
        changeConnectionCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLIChangeConnectionScreen(networkManager, screenManager)));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command startGameCommand = new Command(START_GAME, "Go to Game mode menu");
        startGameCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLISelectGameModeScreen(networkManager, screenManager)));
        commandMap.put(startGameCommand.getCommandText(), startGameCommand);

        Command quitCommand = new Command(QUIT, "Quit game");
        quitCommand.setCommandAction(this::quit);
        commandMap.put(quitCommand.getCommandText(), quitCommand);
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.
                buildGraphicLogo().
                buildMessage("-------------------------Start Game Menu---------------------------").
                buildGraphicHelp(commandMap).
                buildMessage("Choose action: ").toString(), Level.STANDARD);

        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Quit from the game.
     */
    private void quit() {
        screenManager.popScreen();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStartGameScreen has the same commandMap.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStartGameScreen)) return false;
        if (!super.equals(o)) return false;
        CLIStartGameScreen that = (CLIStartGameScreen) o;
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
