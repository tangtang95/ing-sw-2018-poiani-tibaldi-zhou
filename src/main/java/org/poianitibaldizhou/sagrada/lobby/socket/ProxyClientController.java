package org.poianitibaldizhou.sagrada.lobby.socket;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyServerController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.Request;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyClientController implements ILobbyServerController {

    private Socket socket;
    private ILobbyView view;
    private HandleServer serverHandle;

    public ProxyClientController(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            System.out.println(e);
        }
        serverHandle = new HandleServer(socket);
        new Thread(serverHandle).start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void setView(ILobbyView view){
        this.view = view;
    }

    @Override
    public String login(String username, ILobbyView view) throws RemoteException {
        List<Object> parameters = new ArrayList<>();
        parameters.add(username);
        parameters.add(view);
        serverHandle.sendRequest(new Request("login", parameters));
        setView(view);
        String token = (String) serverHandle.getResponse();
        this.view.ack(token);
        return token;
    }

    @Override
    public void logout(String token) throws RemoteException {

    }

    @Override
    public void leave(String token, String username) throws RemoteException {

    }

    @Override
    public Lobby join(String token, String username, ILobbyObserver lobbyObserver) throws RemoteException {
        List<Object> parameters = new ArrayList<>();
        parameters.add(token);
        parameters.add(username);
        parameters.add(lobbyObserver);
        serverHandle.sendRequest(new Request("join", parameters));
        view.ack("You're now in the lobby");
        Lobby lobby = (Lobby) serverHandle.getResponse();
        Logger.getAnonymousLogger().log(Level.INFO, ""+lobby);
        lobby.observeLobby(lobbyObserver);
        return lobby;
    }
}
