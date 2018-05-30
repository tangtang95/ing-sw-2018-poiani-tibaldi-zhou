package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.io.IOException;
import java.util.List;

public interface IDraftPoolFakeObserver  {

    /**
     * Notify to the observers that a certain dice has been added to the DraftPool
     *
     * @param dice dice that's been added
     * @throws IOException network error
     */
    void onDiceAdd(Dice dice) throws IOException;

    /**
     * Notify that a certain dice has been removed from the DraftPool
     *
     * @param dice removed dice
     * @throws IOException network error
     */
    void onDiceRemove(Dice dice) throws IOException;

    /**
     * Notify that a list of dices has been added to the DraftPool.
     *
     * @param dices list of dices added
     * @throws IOException network error
     */
    void onDicesAdd(List<Dice> dices) throws IOException;

    /**
     * Notify that DraftPool has been entirely re-rolled.
     *
     * @param dices list of dices in the DraftPool after that the re-roll action has been
     *              performed
     * @throws IOException network error
     */
    void onDraftPoolReroll(List<Dice> dices) throws IOException;

    /**
     * Notify that the DraftPool has been cleared and that's, therefore, empty.
     *
     * @throws IOException network error
     */
    void onDraftPoolClear() throws IOException;
}