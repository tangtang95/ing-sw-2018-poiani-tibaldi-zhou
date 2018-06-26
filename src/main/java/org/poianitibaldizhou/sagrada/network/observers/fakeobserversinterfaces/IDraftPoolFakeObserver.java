package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.util.List;

public interface IDraftPoolFakeObserver {

    /**
     * Notify to the observers that a certain dice has been added to the DraftPool
     *
     * @param dice dice that's been added
     */
    void onDiceAdd(Dice dice);

    /**
     * Notify that a certain dice has been removed from the DraftPool
     *
     * @param dice removed dice
     */
    void onDiceRemove(Dice dice);

    /**
     * Notify that a list of dices has been added to the DraftPool.
     *
     * @param dices list of dices added
     */
    void onDicesAdd(List<Dice> dices);

    /**
     * Notify that DraftPool has been entirely re-rolled.
     *
     * @param dices list of dices in the DraftPool after that the re-roll action has been
     *              performed
     */
    void onDraftPoolReroll(List<Dice> dices);

    /**
     * Notify that the DraftPool has been cleared and that's, therefore, empty.
     */
    void onDraftPoolClear();
}