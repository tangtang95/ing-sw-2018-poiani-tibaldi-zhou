package org.poianitibaldizhou.sagrada.lobby;

import org.poianitibaldizhou.sagrada.lobby.controller.GameServerControllerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        GameServerControllerRMI controller = new GameServerControllerRMI();
        System.out.println(">>> Controller exported");

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("lobbycontroller", controller);
    }
}
