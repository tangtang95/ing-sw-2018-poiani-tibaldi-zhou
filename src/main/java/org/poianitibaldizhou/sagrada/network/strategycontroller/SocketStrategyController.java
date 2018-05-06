package org.poianitibaldizhou.sagrada.network.strategycontroller;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyGameController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyLobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ServerHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketStrategyController implements StrategyController{

    private ProxyLobbyController lobbyController;
    private ProxyGameController gameController;
    private Socket socket;
    private ServerHandler serverHandler;

    /**
     * Constructor.
     * Create a socket strategy controller that gives back the proxy controller requested; Create a unique
     * server handler for every controller and start it, then create each controller
     *
     * @param ipAddress the ip address of the server
     * @param port the listening port of the server socket
     */
    public SocketStrategyController(String ipAddress, int port){
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
        serverHandler = new ServerHandler(socket);
        new Thread(serverHandler).start();
        lobbyController = new ProxyLobbyController(serverHandler);
        gameController = new ProxyGameController(serverHandler);
    }

    @Override
    public ILobbyController getLobbyController() {
        return lobbyController;
    }

    @Override
    public IGameController getGameController() {
        return gameController;
    }
}
