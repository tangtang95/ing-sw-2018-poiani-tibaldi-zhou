package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class CLIGameView extends CLIMenuView implements IGameView {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);

    }

    @Override
    public void run() {
        bufferManager.formatPrint("-----------------------------WELCOME-------------------------------",
                Level.LOW);


    }

    @Override
    public void ack(String ack) throws RemoteException {

    }

    @Override
    public void err(String err) throws RemoteException {

    }
}
