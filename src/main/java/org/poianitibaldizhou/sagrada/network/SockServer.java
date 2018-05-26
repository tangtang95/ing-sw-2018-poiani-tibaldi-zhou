package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.ControllerManager;
import org.poianitibaldizhou.sagrada.ManagerMediator;
import org.poianitibaldizhou.sagrada.game.controller.GameController;
import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class SockServer {

    public static final int SERVER_PORT = 9090;

    public static void main(String[] args) throws RemoteException {
        ManagerMediator managerMediator = new ManagerMediator();
        LobbyController lobbyController = new LobbyController(managerMediator.getLobbyManager());
        GameController gameController = new GameController(managerMediator.getGameManager());
        ControllerManager controllerManager = new ControllerManager(gameController, lobbyController);
        System.out.println(">>> Controller exported");
        ServerSocket serverSocket = null;
        boolean isOnline = true;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            while (isOnline) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(">>> socket client accepted");
                new Thread(new ClientHandler(clientSocket, controllerManager)).start();
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
    }

}
