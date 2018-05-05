package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;
import org.poianitibaldizhou.sagrada.network.NetworkManager;
import org.poianitibaldizhou.sagrada.network.NetworkType;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ClientApp {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        NetworkManager networkManager = new NetworkManager("localhost", NetworkType.RMI);
        ScreenManager screenManager = new ScreenManager();
        screenManager.pushScreen(new CLILobbyView(networkManager,screenManager));
        System.exit(0);
    }

}
