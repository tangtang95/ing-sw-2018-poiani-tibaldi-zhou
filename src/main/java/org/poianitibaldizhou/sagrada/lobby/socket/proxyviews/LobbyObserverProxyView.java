package org.poianitibaldizhou.sagrada.lobby.socket.proxyviews;

import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.socket.HandleClient;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVER-SIDE
 */
public class LobbyObserverProxyView extends ProxyView implements ILobbyObserver {

    private ILobbyObserver clientObserver;

    public LobbyObserverProxyView(HandleClient clientHandler, ILobbyObserver clientObserver) {
        super(clientHandler);
        this.clientObserver = clientObserver;
    }

    @Override
    public void onUserJoin(User user) throws RemoteException {
        List<Object> parameters = new ArrayList<>();
        parameters.add(user);
        clientHandler.sendResponse(new NotifyMessage(clientObserver, "join", parameters));
    }

    @Override
    public void onUserExit(User user) throws RemoteException {

    }

    @Override
    public void onGameStart() throws RemoteException {

    }
}
