package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;
public interface IToolCardObserver extends Remote {

    /**
     * Notify the change of the tokens
     *
     * @param message the protocol message containing the new number of tokens
     * @throws IOException network error
     */
    void onTokenChange(String message) throws IOException;

    /**
     * Notify the destruction of the toolCard
     *
     * @throws IOException network error
     */
    void onCardDestroy() throws IOException;
}

