package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

public class CLIChangeConnectionView extends CLIBasicView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String GO_BACK = "Go back";

    CLIChangeConnectionView(ConnectionManager networkManager, ScreenManager screenManager) throws RemoteException {
        super(networkManager, screenManager);

        initializeCommands();

    }

    private void initializeCommands() {
        Command changeConnectionCommand = new Command(
                networkManager.getNetworkType() == (ConnectionType.RMI) ?
                        ConnectionType.SOCKET.name() : ConnectionType.RMI.name(),
                "Change the connection mode");
        changeConnectionCommand.setCommandAction(() -> changeConnection(changeConnectionCommand.getCommandText()));
        commandMap.put(changeConnectionCommand.getCommandText(), changeConnectionCommand);

        Command goBackCommand = new Command(GO_BACK, "Go to Start Game Menu");
        goBackCommand.setCommandAction(screenManager::popScreen);
        commandMap.put(goBackCommand.getCommandText(), goBackCommand);
    }

    @Override
    public void run() {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                buildMessage("----------------------Select Connection Menu-----------------------").
                buildMessage("Current connection mode: " + networkManager.getNetworkType().name()).
                buildGraphicHelp(commandMap).
                buildMessage("Change connection mode or go to Start Game Menu: ").
                toString(), Level.STANDARD);

        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
    }

    private void changeConnection(String type) {
        networkManager.setNetworkType(ConnectionType.valueOf(type));
        screenManager.popScreen();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIChangeConnectionView)) return false;
        if (!super.equals(o)) return false;
        CLIChangeConnectionView that = (CLIChangeConnectionView) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
