package org.poianitibaldizhou.sagrada.lobby.socket.proxyviews;

import org.poianitibaldizhou.sagrada.lobby.socket.ClientHandler;

/**
 * Proxy Pattern for the view of the client
 * SERVER-SIDE
 * Each proxy view is a proxy to the view of the client
 */
public abstract class ProxyView {

    protected final ClientHandler clientHandler;
    protected final int clientObserverHashcode;

    protected ProxyView(ClientHandler clientHandler, int clientObserverHashcode){
        this.clientHandler = clientHandler;
        this.clientObserverHashcode = clientObserverHashcode;
    }

}
