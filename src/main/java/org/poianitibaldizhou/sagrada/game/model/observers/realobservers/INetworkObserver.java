package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface INetworkObserver extends Remote {

    /**
     * Signal the disconnection of a certain player
     *
     * @param message protocol message containing the disconnected player
     */
    void signalDisconnection(String message) throws IOException;
}
