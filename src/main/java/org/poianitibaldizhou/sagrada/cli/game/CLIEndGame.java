package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.rmi.RemoteException;

public class CLIEndGame extends CLIBasicScreen {

    /**
     * End turn command.
     */
    private static final String QUIT = "Quit game";

    /**
     * constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager     manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIEndGame(ConnectionManager connectionManager, ScreenManager screenManager) throws RemoteException {
        super(connectionManager, screenManager);

        initializeCommands();
    }

    /**
     * Initialize command.
     */
    @Override
    protected void initializeCommands() {
        Command quit = new Command(QUIT, ClientMessage.QUIT_GAME);
        quit.setCommandAction(screenManager::popScreen);
        commandMap.put(quit.getCommandText(), quit);
    }

    /**
     * Start the EndGame screen.
     */
    @Override
    public void startCLI() {
        ConsoleListener.getInstance().setCommandMap(commandMap);
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                buildMessage(ClientMessage.END_GAME).
                buildGraphicHelp(commandMap).
                buildMessage(ClientMessage.CHOOSE_ACTION).toString(), Level.STANDARD);
    }
}
