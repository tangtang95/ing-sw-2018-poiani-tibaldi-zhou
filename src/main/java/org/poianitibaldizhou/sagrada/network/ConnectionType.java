package org.poianitibaldizhou.sagrada.network;


import org.poianitibaldizhou.sagrada.ServerApp;

/**
 * OVERIVEW: representes the type of connections available for the various clients
 */
public enum ConnectionType {
    RMI(ServerApp.SERVER_RMI_PORT), SOCKET(ServerApp.SERVER_SOCKET_PORT);

    private final int port;

    /**
     * Constructor.
     * The port changes if the type of connection changes
     * @param port the port of the specific type of connection
     */
    ConnectionType(int port){
        this.port = port;
    }

    /**
     * Returns the port on which the connection will be active
     * @return port on which the connection will be active
     */
    public int getPort(){
        return port;
    }

}
