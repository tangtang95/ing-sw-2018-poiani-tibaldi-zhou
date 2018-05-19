package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
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

    public CLIStartGameMenuView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);
        initializeCommands();
    }

    private void initializeCommands() {
        Command changeConnectionCommand = new Command(CHANGE_CONNECTION_MODE, "Go to Change connection menu");
        changeConnectionCommand.setCommandAction(() ->
            screenManager.pushScreen(new CLIChangeConnectionMenuView(networkManager, screenManager)));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command startGameCommand = new Command(START_GAME, "Go to Game mode menu");
        startGameCommand.setCommandAction(() ->
                screenManager.pushScreen(new CLISelectGameModeMenuView(networkManager, screenManager)));
        commandMap.put(startGameCommand.getCommandText(), startGameCommand);

        Command quitCommand = new Command(QUIT, "Quit game");
        quitCommand.setCommandAction(this::quit);
        commandMap.put(quitCommand.getCommandText(), quitCommand);
    }

    private void printLogo() {
        StringBuilder stringBuilder = new StringBuilder();
        int charNumber = 76;

        stringBuilder.append((char) 9556);
        for (int i = 0; i < charNumber; i++) {
            stringBuilder.append((char) 9552);
        }
        stringBuilder.append((char) 9559 + "\n" + (char) 9553);
        for (int i = 0; i < (charNumber - 13) / 3; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append("     S A G R A D A     ");
        for (int i = 0; i < (charNumber - 13) / 3; i++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append((char) 9553 + "\n" + (char) 9562);
        for (int i = 0; i < charNumber; i++) {
            stringBuilder.append((char) 9552);
        }
        stringBuilder.append((char) 9565 + "\n");
        bufferManager.formatPrint(stringBuilder.toString(), Level.LOW);
    }

    @Override
    public void run() {
        printLogo();

        bufferManager.formatPrint("-------------------------Start Game Menu---------------------------",
                Level.LOW);

        help(commandMap);
        try {
            bufferManager.formatPrint("Choose action: ", Level.LOW);
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
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
