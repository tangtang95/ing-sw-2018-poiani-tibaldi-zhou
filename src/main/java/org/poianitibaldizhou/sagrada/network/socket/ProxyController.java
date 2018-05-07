package org.poianitibaldizhou.sagrada.network.socket;

public abstract class ProxyController {

    protected ServerHandler serverHandler;

    /**
     * Constructor.
     * Create a proxy client controller to have transparency of the socket connection
     *
     * @param serverHandler the serverHandler connected with the server
     */
    public ProxyController(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
}
