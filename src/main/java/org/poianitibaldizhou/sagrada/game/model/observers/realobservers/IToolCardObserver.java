package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;
public interface IToolCardObserver extends Remote {

    /**
     * Notify the change of the tokens
     *
     * @param tokens the new number of tokens
     * @throws IOException network error
     */
    void onTokenChange(int tokens) throws IOException;

    /**
     * Notify the destruction of the toolCard
     *
     * @throws IOException network error
     */
    void onCardDestroy() throws IOException;
}

