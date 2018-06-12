package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface ISchemaCardObserver extends Remote {

    /**
     * Notify that a dice's been placed in a certain position
     *
     * @param message string message containing the dice and the position
     * @throws IOException network communication error
     */
    void onPlaceDice(String message) throws IOException;


    /**
     * Notify that a dice's been removed from a certain position
     *
     * @param message
     * @throws IOException network communication error
     */
    void onDiceRemove(String message) throws IOException;
}
