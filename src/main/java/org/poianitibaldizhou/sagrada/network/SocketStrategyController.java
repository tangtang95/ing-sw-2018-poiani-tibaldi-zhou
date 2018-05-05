package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyGameController;
import org.poianitibaldizhou.sagrada.network.socket.ProxyLobbyController;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketStrategyController implements StrategyController{

    private ProxyLobbyController lobbyController;
    private ProxyGameController gameController;
    private Socket socket;

    public SocketStrategyController(String ipAddress, int port){
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
        lobbyController = new ProxyLobbyController(socket);
        gameController = new ProxyGameController(socket);
    }

    @Override
    public ILobbyController getLobbyController() {
        disableControllers();
        lobbyController = new ProxyLobbyController(socket);
        return lobbyController;
    }

    @Override
    public IGameController getGameController() {
        disableControllers();
        gameController = new ProxyGameController(socket);
        return gameController;
    }

    private void disableControllers(){
        lobbyController.close();
        gameController.close();
    }
}
