package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

@Deprecated
public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {

        ScreenManager screenManager = new ScreenManager();
        }
}
