package org.poianitibaldizhou.sagrada.lobby;

import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.socket.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class SockServer {

    public static final int SERVER_PORT = 9090;

    public static void main(String[] args) throws RemoteException {
        ServerSocket serverSocket = null;
        ILobbyController serverController = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            serverController = new LobbyController();
            while (serverSocket != null){
                Socket clientSocket = serverSocket.accept();
                System.out.println(">>> client accepted");
                new Thread(new ClientHandler(clientSocket, serverController)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serverSocket != null)
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
