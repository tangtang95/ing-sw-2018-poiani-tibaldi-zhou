package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.NetworkManager;
import org.poianitibaldizhou.sagrada.network.NetworkType;

import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Logger;

public class CLIChangeConnectionMenuView extends CLIMenuView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private static final String GO_BACK = "Go back";

    CLIChangeConnectionMenuView(NetworkManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super(networkManager, screenManager, bufferManager);
        initializeCommands();
    }

    private void initializeCommands() {
        Command changeConnectionCommand = new Command(
                networkManager.getNetworkType().name().equals("RMI") ? "SOCKET" : "RMI",
                "Change the connection mode");
        changeConnectionCommand.setCommandAction(this::changeConnection);
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
                toString(), Level.LOW);
        try {
            getCommand(commandMap).executeCommand();
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        } catch (NullPointerException e) {
            //...
        }
    }

    private void changeConnection() {
        networkManager.setNetworkType(NetworkType.valueOf(commandMap.keySet().toArray()[0].toString()));
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