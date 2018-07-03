package org.poianitibaldizhou.sagrada.network.strategycontroller;

import org.poianitibaldizhou.sagrada.game.controller.GameController;
import org.poianitibaldizhou.sagrada.game.controller.IGameController;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.controller.LobbyController;
import org.poianitibaldizhou.sagrada.network.socket.*;
import org.poianitibaldizhou.sagrada.utilities.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: This allows the access to the controllers via socket
 */
public class SocketStrategyController implements NetworkStrategyController {

    private ILobbyController lobbyController;
    private IGameController gameController;
    private Socket socket;
    private ServerHandler serverHandler;

    /**
     * Constructor.
     * Create a socket strategy controller that gives back the proxy controller requested; Create a unique
     * server handler for every controller and start it, then create each controller
     *
     * @param ipAddress the ip address of the server
     * @param port      the listening port of the server socket
     */
    public SocketStrategyController(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
        serverHandler = new ServerHandler(socket);
        new Thread(serverHandler).start();
        lobbyController = (ILobbyController) Proxy.newProxyInstance(ILobbyController.class.getClassLoader(),
                new Class[]{ILobbyController.class},
                new ProxyControllerInvocationHandler(serverHandler, ControllerType.LOBBY_CONTROLLER));
        gameController = (IGameController) Proxy.newProxyInstance(IGameController.class.getClassLoader(),
                new Class[]{IGameController.class},
                new ProxyControllerInvocationHandler(serverHandler, ControllerType.GAME_CONTROLLER));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ILobbyController getLobbyController() {
        return lobbyController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGameController getGameController() {
        return gameController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.FINE, ServerMessage.SOCKET_CLOSE);
        }
        lobbyController = null;
        gameController = null;
        socket = null;
        serverHandler = null;
    }
}
