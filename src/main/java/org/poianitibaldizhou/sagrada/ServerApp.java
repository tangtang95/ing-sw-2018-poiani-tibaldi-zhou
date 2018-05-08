package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp {
    private static Registry registry;

    public static final int SERVER_SOCKET_PORT = 9090;
    public static final int SERVER_RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {
        LobbyController controller = new LobbyController();
        System.out.println(">>> Controller exported");

        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(SERVER_SOCKET_PORT);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(">>> socket client accepted");
                    new Thread(new ClientHandler(clientSocket, controller)).start();
                }
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        }).start();

        registry = LocateRegistry.getRegistry(SERVER_RMI_PORT);
        registry.rebind("lobbycontroller", controller);
    }
}