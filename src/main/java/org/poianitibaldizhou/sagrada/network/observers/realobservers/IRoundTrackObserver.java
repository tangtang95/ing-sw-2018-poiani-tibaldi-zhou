package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Real observer for the round track. Real observer are observers client side that
 * listen to changes and modification of the model. All the parameters of the methods are
 * protocol string.
 */
public interface IRoundTrackObserver extends Remote {

    /**
     * Notify that a list of dices has been added to a certain round.
     *
     * @param message string message containing the list of dices and the round
     * @throws IOException network error
     */
    void onDicesAddToRound(String message) throws IOException;

    /**
     * Notify that a dice has been added to a certain round.
     *
     * @param message string message containing the dice and the round
     * @throws IOException network error
     */
    void onDiceAddToRound(String message) throws IOException;

    /**
     * Notify that a dice's been removed from a certain round
     *
     * @param message string message containing the the dice and the round
     * @throws IOException network error
     */
    void onDiceRemoveFromRound(String message) throws IOException;

    /**
     * Notify that a dice of a certain round has been removed and that a new dice
     * has been added at its place.
     *
     * @param message string message containing the old dice, the new dice and the round
     * @throws IOException network error
     */
    void onDiceSwap(String message) throws IOException;
}
