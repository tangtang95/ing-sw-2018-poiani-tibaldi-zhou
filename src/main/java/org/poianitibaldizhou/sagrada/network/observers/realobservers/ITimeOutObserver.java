package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Real observer for the time out. Real observer are observers client side that
 * listen to changes and modification of the model. All the parameters of the methods are
 * protocol string.
 */
public interface ITimeOutObserver extends Remote {

    /**
     * Notifies that an user has timed-out.
     * There is a parameter set on the server which allows a player to perform a move
     * only in a certain amount of time.
     *
     * @param message message containing the player who timed out
     * @throws IOException network communication error
     */
    void onTimeOut(String message) throws IOException;
}
