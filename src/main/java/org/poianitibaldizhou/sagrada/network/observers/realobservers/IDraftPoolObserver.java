package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface IDraftPoolObserver extends Remote {

    /**
     * Notify to the observers that a certain dice has been added to the DraftPool
     *
     * @param dice dice that's been added
     * @throws IOException network error
     */
    void onDiceAdd(String dice) throws IOException;

    /**
     * Notify that a certain dice has been removed from the DraftPool
     *
     * @param dice removed dice
     * @throws IOException network error
     */
    void onDiceRemove(String dice) throws IOException;

    /**
     * Notify that a list of dices has been added to the DraftPool.
     *
     * @param dices list of dices added
     * @throws IOException network error
     */
    void onDicesAdd(String dices) throws IOException;

    /**
     * Notify that DraftPool has been entirely re-rolled.
     *
     * @param dices list of dices in the DraftPool after that the re-roll action has been
     *              performed
     * @throws IOException network error
     */
    void onDraftPoolReroll(String dices) throws IOException;

    /**
     * Notify that the DraftPool has been cleared and that's, therefore, empty.
     *
     * @throws IOException network error
     */
    void onDraftPoolClear() throws IOException;
}
