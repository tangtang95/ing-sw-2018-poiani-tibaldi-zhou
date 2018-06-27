package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Real observer for a certain tool card. Real observer are observers client side that
 * listen to changes and modification of the model. All the parameters of the methods are
 * protocol string.
 */
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

