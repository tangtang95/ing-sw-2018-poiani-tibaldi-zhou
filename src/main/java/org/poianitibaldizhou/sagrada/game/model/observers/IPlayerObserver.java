package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.players.Outcome;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayerObserver extends Remote{

    /**
     * Notify that the tokens of a certain player has changed
     *
     * @param value new value of the player tokens
     * @throws RemoteException network error
     */
    void onFavorTokenChange(int value) throws RemoteException;

    /**
     * Notify the change of outcome of the game to the player
     *
     * @param outcome new outcome of the player
     * @throws RemoteException network error
     */
    void onSetOutcome(Outcome outcome) throws RemoteException;
}
