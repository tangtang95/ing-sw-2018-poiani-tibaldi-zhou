package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIStartApplicationScreen;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.ConnectionType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp {

    public static void main(String[] args) {
        ConnectionManager networkManager = new ConnectionManager(ClientSettings.getIP(), ConnectionType.RMI.getPort(),
                ConnectionType.RMI);
        ScreenManager screenManager = new ScreenManager();

        try {
            screenManager.pushScreen(new CLIStartApplicationScreen(networkManager,screenManager));
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "CLI cannot initialize");
        }
    }

}
