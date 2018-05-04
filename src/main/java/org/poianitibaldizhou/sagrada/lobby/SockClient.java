package org.poianitibaldizhou.sagrada.lobby;

import org.poianitibaldizhou.sagrada.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.socket.ProxyClientController;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.rmi.RemoteException;

public class SockClient {
    public static void main(String[] args) {
        ProxyClientController proxy = new ProxyClientController("localhost", SockServer.SERVER_PORT);
        ScreenManager screenManager = new ScreenManager();
        try {
            screenManager.pushScreen(new CLILobbyView(proxy,screenManager));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
