package org.poianitibaldizhou.sagrada;

import org.poianitibaldizhou.sagrada.game.controller.GameController;
import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.socket.ClientHandler;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServerTestApp {

    private static final int SERVER_SOCKET_PORT = 9090;
    private static final int SERVER_RMI_PORT = 1099;

    public static void main(String[] args) throws RemoteException {
        MediatorManager managerMediator = new MediatorManager();
        List<User> users = new ArrayList<>();
        users.add(new User("cordero1", NetworkUtility.encrypt("cordero1")));
        users.add(new User("cordero2", NetworkUtility.encrypt("cordero2")));
        managerMediator.createMultiPlayerGameTest(users);
        LobbyController lobbyController = new LobbyController(managerMediator.getLobbyManager());
        GameController gameController = new GameController(managerMediator.getGameManager());
        ControllerManager controllerManager = new ControllerManager(gameController, lobbyController);
        System.out.println(">>> GraphicsController exported");

        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(SERVER_SOCKET_PORT);
                while (true) {
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
        }).start();

        Registry registry = LocateRegistry.getRegistry(SERVER_RMI_PORT);
        registry.rebind("lobbycontroller", controllerManager.getLobbyController());
        registry.rebind("gamecontroller", controllerManager.getGameController());
    }
}
