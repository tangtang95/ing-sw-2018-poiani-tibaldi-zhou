package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;
import org.poianitibaldizhou.sagrada.network.NetworkManager;
import org.poianitibaldizhou.sagrada.network.NetworkType;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApp {

    public static void main(String[] args) {
        NetworkManager networkManager = new NetworkManager("localhost", NetworkType.SOCKET);
        ScreenManager screenManager = new ScreenManager();
        try {
            screenManager.pushScreen(new CLILobbyView(networkManager,screenManager));
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "CLILobbyView cannot initialize");
        }
    }

}
