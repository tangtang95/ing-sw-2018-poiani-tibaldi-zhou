package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.io.IOException;
import java.rmi.RemoteException;

@Deprecated
public class SockClient {
    public static void main(String[] args) {
        ScreenManager screenManager = new ScreenManager();
        try {
            screenManager.pushScreen(new CLILobbyView(
                    new ConnectionManager("localhost", ConnectionType.SOCKET), screenManager));
        } catch (RemoteException e) {
            //..
        }
    }
}
