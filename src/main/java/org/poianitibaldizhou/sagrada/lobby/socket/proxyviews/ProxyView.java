package org.poianitibaldizhou.sagrada.lobby.socket.proxyviews;

import org.poianitibaldizhou.sagrada.lobby.socket.HandleClient;

/**
 * SERVER-SIDE
 */
public abstract class ProxyView {

    protected HandleClient clientHandler;

    protected ProxyView(HandleClient clientHandler){
        this.clientHandler = clientHandler;
    }

}
