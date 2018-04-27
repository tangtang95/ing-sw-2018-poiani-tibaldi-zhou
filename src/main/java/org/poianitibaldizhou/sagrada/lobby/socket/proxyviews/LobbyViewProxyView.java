package org.poianitibaldizhou.sagrada.lobby.socket.proxyviews;

import org.poianitibaldizhou.sagrada.lobby.socket.HandleClient;
import org.poianitibaldizhou.sagrada.lobby.socket.messages.NotifyMessage;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVER-SIDE
 */
public class LobbyViewProxyView extends ProxyView implements ILobbyView {

    private ILobbyView clientView;

    protected LobbyViewProxyView(HandleClient clientHandler) {
        super(clientHandler);
    }

    @Override
    public void ack(String ack) throws RemoteException {
        List<Object> parameters = new ArrayList<>();
        parameters.add(ack);
        clientHandler.sendResponse(new NotifyMessage(clientView, "ack", parameters));
    }
}
