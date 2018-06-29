package org.poianitibaldizhou.sagrada.cli.game;

import javafx.application.Application;
import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.graphics.ClientGUIApp;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.rmi.RemoteException;

public class CLIGraphicModeScreen extends CLIBasicScreen{

    /**
     * constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager     manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIGraphicModeScreen(ConnectionManager connectionManager, ScreenManager screenManager) throws RemoteException {
        super(connectionManager, screenManager);
        initializeCommands();
    }

    /**
     * Initialize the StartGame's commands.
     */
    @Override
    protected void initializeCommands() {
        Command guiCommand = new Command(ClientMessage.GUI,
                ClientMessage.GUI_HELP);
        guiCommand.setCommandAction(this::startGUI);
        commandMap.put(guiCommand.getCommandText(), guiCommand);

        Command cliCommand = new Command(ClientMessage.CLI, ClientMessage.CLI_HELP);
        cliCommand.setCommandAction(() ->
                screenManager.replaceScreen(new CLIStartApplicationScreen(connectionManager, screenManager)));
        commandMap.put(cliCommand.getCommandText(), cliCommand);
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.
                buildMessage(ClientMessage.WELCOME).
                buildGraphicHelp(commandMap).
                buildMessage(ClientMessage.CHOOSE_START_MODE).toString(), Level.STANDARD);

        consoleListener.setCommandMap(commandMap);
    }

    /**
     * Start the GUI.
     */
    private void startGUI() {
        Thread thread = new Thread(() -> Application.launch(ClientGUIApp.class));
        thread.start();
    }
}
