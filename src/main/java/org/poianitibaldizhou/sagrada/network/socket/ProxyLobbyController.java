package org.poianitibaldizhou.sagrada.network.socket;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

/**
 * Proxy Pattern for the controller of the server
 * CLIENT-SIDE
 */
public class ProxyLobbyController extends ProxyController implements ILobbyController {

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param serverHandler the serverHandler connected with the server
     */
    public ProxyLobbyController(ServerHandler serverHandler) {
        super(serverHandler);
    }

    /**
     * Counter-part method login of the server-side controller; need to add the view to the hashMap of the
     * server handler and send the request via serverHandler
     *
     * @param username user's name
     * @param view     view's name
     * @return the token connected to the username
     */
    @Override
    public String login(String username, ILobbyView view) {
        serverHandler.addViewToHashMap(view.hashCode(), view);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, username, view));
        return (String) serverHandler.getResponse();
    }

    /**
     * Counter-part method logout of the server-side controller; send the request via serverHandler
     *
     * @param token the token of the user requesting logout
     */
    @Override
    public void logout(String token) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, token));
    }

    /**
     * Counter-part method leave of the server-side controller; send the request via serverHandler
     *
     * @param token    the token of the user requesting to leave the lobby
     * @param username the username of the user requesting to leave the lobby
     */
    @Override
    public void leave(String token, String username) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, token, username));
    }

    /**
     * Counter-part method login of the server-side controller; need to add the observer to the hashMap of the
     * server handler and send the request via serverHandler
     *
     * @param token         the user's token
     * @param username      the user's username
     * @param lobbyObserver the observer of the lobby
     */
    @Override
    public void join(String token, String username, ILobbyObserver lobbyObserver) {
        serverHandler.addViewToHashMap(lobbyObserver.hashCode(), lobbyObserver);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, token, username, lobbyObserver));
    }

    /**
     * Send a request of the user currently present in the lobby.
     *
     * @param token requesting user's token
     */
    @Override
    public void requestUsersInLobby(String token) {
        String methodName= Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, token));
    }

    /**
     * Requesting time to reach timeout
     *
     * @param token requesting user's token
     */
    @Override
    public void requestTimeout(String token) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        serverHandler.sendRequest(new Request(methodName, token));
    }
}
