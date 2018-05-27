package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IDraftPoolObserver extends Remote {

    /**
     * Notify to the observers that a certain dice has been added to the DraftPool
     *
     * @param dice dice that's been added
     * @throws RemoteException network error
     */
    void onDiceAdd(Dice dice) throws RemoteException;

    /**
     * Notify that a certain dice has been removed from the DraftPool
     *
     * @param dice removed dice
     * @throws RemoteException network error
     */
    void onDiceRemove(Dice dice) throws RemoteException;

    /**
     * Notify that a list of dices has been added to the DraftPool.
     *
     * @param dices list of dices added
     * @throws RemoteException network error
     */
    void onDicesAdd(List<Dice> dices) throws RemoteException;

    /**
     * Notify that DraftPool has been entirely re-rolled.
     *
     * @param dices list of dices in the DraftPool after that the re-roll action has been
     *              performed
     * @throws RemoteException network error
     */
    void onDraftPoolReroll(List<Dice> dices) throws RemoteException;

    /**
     * Notify that the DraftPool has been cleared and that's, therefore, empty.
     *
     * @throws RemoteException network error
     */
    void onDraftPoolClear() throws RemoteException;
}
