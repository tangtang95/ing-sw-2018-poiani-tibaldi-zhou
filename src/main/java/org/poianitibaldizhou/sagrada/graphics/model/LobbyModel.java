package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains the values used in the lobby phase of sagrada. Furthermore, it makes requests to the server to
 * get or pass information
 */
public class LobbyModel {

    private ConnectionManager connectionManager;
    private final String username;
    private String token;

    /**
     * Constructor.
     * Create a lobby model that contains username and the manager of the connection; This object makes
     * requests to the server to get or pass information
     *
     * @param username the username of the client
     * @param connectionManager the manager of the connection
     */
    public LobbyModel(String username, ConnectionManager connectionManager) {
        this.username = username;
        this.connectionManager = connectionManager;
    }

    // GETTER

    /**
     * @return username of the player who is playing with this client
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return token of the player who is playing with this client
     */
    public String getToken() {
        return token;
    }

    /**
     * @return connection manager for managing the connections with the server
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    // SERVER REQUESTS

    /**
     * Login request to the server
     *
     * @param view the view of the client
     * @param observer the lobby observer of the client
     * @throws IOException if there are network errors or the username is already in use
     */
    public void login(ILobbyView view, ILobbyObserver observer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();

        String request = builder.createUsernameMessage(username).buildMessage();
        String response = connectionManager.getLobbyController().login(request, view);
        token = parser.getToken(response);
        if(token == null) throw new IOException(ClientMessage.USER_ALREADY_EXIST);

        request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().join(request, observer);
    }

    /**
     * GetUsers request to the server
     *
     * @return the user in the lobby
     * @throws IOException network error
     */
    public List<UserWrapper> getUsers() throws IOException {
        ClientGetMessage parser = new ClientGetMessage();

        String response = connectionManager.getLobbyController().getUsersInLobby();
        return parser.getListOfUserWrapper(response);
    }

    /**
     * Leave request to the server
     *
     * @throws IOException network error
     */
    public void leave() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().leave(request);
    }

    /**
     * GetTimeout request to the server
     *
     * @return the timeout value in millis
     * @throws IOException network error
     */
    public int getTimeout() throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        String timeoutText = parser.getTimeout(connectionManager.getLobbyController().getTimeout());
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            Date dt = formatter.parse(timeoutText);
            cal.setTime(dt);
        }catch (ParseException ex){
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.toString());
        }
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        return  minute*60 + second;
    }
}
