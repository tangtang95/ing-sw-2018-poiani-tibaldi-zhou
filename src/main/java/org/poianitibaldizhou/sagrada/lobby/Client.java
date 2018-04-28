package org.poianitibaldizhou.sagrada.lobby;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();

        ILobbyController controller = (ILobbyController) registry.lookup("lobbycontroller");

        new CLILobbyView(controller).run();
    }
}
