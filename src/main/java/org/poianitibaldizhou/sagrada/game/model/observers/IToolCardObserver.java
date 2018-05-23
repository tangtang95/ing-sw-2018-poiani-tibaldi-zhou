package org.poianitibaldizhou.sagrada.game.model.observers;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IToolCardObserver extends Remote {

    /**
     * Notify the change of the tokens
     *
     * @param tokens the new number of tokens
     * @throws RemoteException network error
     */
    void onTokenChange(int tokens) throws RemoteException;

    /**
     * Notify the destruction of the toolCard
     *
     * @throws RemoteException network error
     */
    void onCardDestroy() throws RemoteException;
}

