package org.poianitibaldizhou.sagrada.graphics.model;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LobbyModel {

    private ConnectionManager connectionManager;
    private final String username;
    private String token;

    public LobbyModel(String username, ConnectionManager connectionManager) {
        this.username = username;
        this.connectionManager = connectionManager;
    }

    public void login(IView view, ILobbyObserver observer) throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();

        String request = builder.createUsernameMessage(username).buildMessage();
        String response = connectionManager.getLobbyController().login(request, view);
        token = parser.getToken(response);

        request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().join(request, observer);
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public List<UserWrapper> requestGetUsers() throws IOException {
        ClientGetMessage parser = new ClientGetMessage();

        String response = connectionManager.getLobbyController().getUsersInLobby();
        return parser.getListOfUserWrapper(response);
    }

    public void leave() throws IOException {
        ClientCreateMessage builder = new ClientCreateMessage();
        String request = builder.createTokenMessage(token).createUsernameMessage(username).buildMessage();
        connectionManager.getLobbyController().leave(request);
        connectionManager.close();
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

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
