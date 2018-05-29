package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CLIChangeConnectionMenuView extends CLIMenuView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String GO_BACK = "Go back";

    CLIChangeConnectionMenuView(ConnectionManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super(networkManager, screenManager, bufferManager);
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
        bufferManager.consolePrint(buildGraphic.
                buildMessage("----------------------Select Connection Menu-----------------------").
                buildMessage("Current connection mode: " + networkManager.getNetworkType().name()).
                buildGraphicHelp(commandMap).
                buildMessage("Change connection mode or go to Start Game Menu: ").
                toString(), Level.STANDARD);
        try {
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        } catch (NullPointerException e) {
            //...
        }
    }

    private void changeConnection(String type) {
        networkManager.setNetworkType(ConnectionType.valueOf(type));
        screenManager.popScreen();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIChangeConnectionMenuView)) return false;
        if (!super.equals(o)) return false;
        CLIChangeConnectionMenuView that = (CLIChangeConnectionMenuView) o;
        return Objects.equals(commandMap, that.commandMap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), commandMap);
    }
}
