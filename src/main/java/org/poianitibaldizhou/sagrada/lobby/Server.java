package org.poianitibaldizhou.sagrada.lobby;

import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        LobbyController controller = new LobbyController();
        System.out.println(">>> Controller exported");

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("lobbycontroller", controller);
    }
}
