package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyScreen;

import java.rmi.RemoteException;

@Deprecated
public class SockClient {
    public static void main(String[] args) {
        ScreenManager screenManager = new ScreenManager();
        try {
            screenManager.pushScreen(new CLILobbyScreen(
                    new ConnectionManager("localhost", ConnectionType.SOCKET), screenManager));
        } catch (RemoteException e) {
            //..
        }
    }
}
