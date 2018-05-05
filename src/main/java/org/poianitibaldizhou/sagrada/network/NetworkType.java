package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.ServerApp;

public enum NetworkType {
    RMI(ServerApp.SERVER_RMI_PORT), SOCKET(ServerApp.SERVER_SOCKET_PORT);

    private final int port;

    NetworkType(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

}
