package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.controller.GameServerControllerSocket;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyServerController;

import java.net.Socket;
import java.rmi.RemoteException;

public class ProxyServerController {

    private ILobbyServerController controllerSocket;
    private Socket clientSocket;

    public ProxyServerController(Socket clientSocket, ILobbyServerController controllerSocket) {
        this.clientSocket = clientSocket;
        this.controllerSocket = controllerSocket;
        new Thread(new HandleClient(clientSocket, controllerSocket)).start();
    }
}
