package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIStartGameMenuView;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp {

    public static void main(String[] args) {
        ConnectionManager networkManager = new ConnectionManager("localhost", ConnectionType.SOCKET);
        ScreenManager screenManager = new ScreenManager();
        BufferManager bufferManager = new BufferManager();

        try {
            screenManager.pushScreen(new CLIStartGameMenuView(networkManager,screenManager, bufferManager));
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "CLI cannot initialize");
        }
    }

}
