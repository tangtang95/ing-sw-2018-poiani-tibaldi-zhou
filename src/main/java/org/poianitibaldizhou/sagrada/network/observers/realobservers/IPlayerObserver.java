package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Real observer for a certain player. Real observer are observers client side that
 * listen to changes and modification of the model. All the parameters of the methods are
 * protocol string.
 */
public interface IPlayerObserver extends Remote{

    /**
     * Notify that the tokens of a certain player has changed
     *
     * @param value the cost variation
     * @throws IOException network error
     */
    void onFavorTokenChange(String value) throws IOException;

    /**
     * Notify the change of outcome of the game to the player
     *
     * @param outcome new outcome of the player
     * @throws IOException network error
     */
    void onSetOutcome(String outcome) throws IOException;
}
