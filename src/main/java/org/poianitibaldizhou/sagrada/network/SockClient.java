package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.rmi.RemoteException;

public class SockClient {
    public static void main(String[] args) {
        ScreenManager screenManager = new ScreenManager();
        try {
            screenManager.pushScreen(new CLILobbyView(
                    new NetworkManager("localhost", NetworkType.SOCKET),screenManager));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
