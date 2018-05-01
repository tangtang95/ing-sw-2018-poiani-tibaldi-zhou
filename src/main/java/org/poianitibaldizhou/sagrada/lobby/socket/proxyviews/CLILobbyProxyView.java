package org.poianitibaldizhou.sagrada.lobby.socket.proxyviews;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.socket.ClientHandler;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Proxy Pattern for the CLIView of the client
 * SERVER-SIDE
 */
public class CLILobbyProxyView extends ProxyView implements ILobbyObserver, ILobbyView {

    /**
     * Constructor.
     * Create the proxy of CLILobbyView of the client
     *
     * @param clientHandler  the clientHandler of the server
     * @param clientObserverHashcode the CLILobbyView passed via socket
     */
    public CLILobbyProxyView(ClientHandler clientHandler, int clientObserverHashcode) {
        super(clientHandler, clientObserverHashcode);
    }

    /**
     * Counter-part method onUserJoin of the client-side view
     *
     * @param user the user who has joined
     */
    @Override
    public void onUserJoin(User user) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<Object> parameters = new ArrayList<>();
        parameters.add(user);
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, methodName, parameters));
    }

    /**
     * Counter-part method onUserExit of the client-side view
     *
     * @param user the user who has left the lobby
     */
    @Override
    public void onUserExit(User user) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<Object> parameters = new ArrayList<>();
        parameters.add(user);
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, methodName, parameters));
    }

    /**
     * Counter-part method onGameStart of the client-side view
     *
     */
    @Override
    public void onGameStart() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<Object> parameters = new ArrayList<>();
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, methodName, parameters));
    }

    /**
     * Counter-part method ack of the client-side view
     *
     * @param ack the ack string to send to the client
     */
    @Override
    public void ack(String ack) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<Object> parameters = new ArrayList<>();
        parameters.add(ack);
        clientHandler.sendResponse(new NotifyMessage(clientObserverHashcode, methodName, parameters));
    }

}
