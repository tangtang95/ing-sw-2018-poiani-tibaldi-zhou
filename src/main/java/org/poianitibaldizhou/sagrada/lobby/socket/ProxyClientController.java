package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy Pattern for the controller of the server
 * CLIENT-SIDE
 */
public class ProxyClientController implements ILobbyController {

    private Socket socket;
    private ServerHandler serverHandler;

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param ipAddress the IP address of the server
     * @param port      the port of the server on which is listening
     */
    public ProxyClientController(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            System.out.println(e);
        }
        serverHandler = new ServerHandler(socket);
        new Thread(serverHandler).start();
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
        List<Object> parameters = new ArrayList<>();
        parameters.add(username);
        parameters.add(view);
        serverHandler.sendRequest(new Request(methodName, parameters));
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
        List<Object> parameters = new ArrayList<>();
        parameters.add(token);
        serverHandler.sendRequest(new Request(methodName, parameters));
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
        List<Object> parameters = new ArrayList<>();
        parameters.add(token);
        parameters.add(username);
        serverHandler.sendRequest(new Request(methodName, parameters));
    }

    /**
     * Counter-part method login of the server-side controller; need to add the observer to the hashMap of the
     * server handler and send the request via serverHandler
     *
     * @param token         the user's token
     * @param username      the user's username
     * @param lobbyObserver the observer of the lobby
     * @return the lobby joined
     */
    @Override
    public void join(String token, String username, ILobbyObserver lobbyObserver) {
        serverHandler.addViewToHashMap(lobbyObserver.hashCode(), lobbyObserver);
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        List<Object> parameters = new ArrayList<>();
        parameters.add(token);
        parameters.add(username);
        parameters.add(lobbyObserver);
        serverHandler.sendRequest(new Request(methodName, parameters));
    }
}
