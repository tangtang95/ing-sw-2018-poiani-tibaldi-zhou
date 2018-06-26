package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.ClientSettings;

/**
 * OVERVIEW: representes the type of connections available for the various clients
 */
public enum ConnectionType {
    RMI(ClientSettings.getRMIPort()), SOCKET(ClientSettings.getSocketPort());

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
