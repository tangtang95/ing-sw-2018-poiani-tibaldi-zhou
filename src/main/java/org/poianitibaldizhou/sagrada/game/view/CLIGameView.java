package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;

public class CLIGameView extends CLIMenuView implements IGameView {

    public CLIGameView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);
    }

    @Override
    public void run() {
        System.out.println("-----------------------------WELCOME-------------------------------");
    }

    @Override
    public void ack(String ack) throws RemoteException {

    }

    @Override
    public void err(String err) throws RemoteException {

    }
}
