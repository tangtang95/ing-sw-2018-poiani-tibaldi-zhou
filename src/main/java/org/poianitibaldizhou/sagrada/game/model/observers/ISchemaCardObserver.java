package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;

public interface ISchemaCardObserver {

    /**
     * Notify that a dice's been placed in a certain position
     *
     * @param dice     dice placed
     * @param position placement position
     */
    void onPlaceDice(Dice dice, Position position);


    /**
     * Notify that a dice's been removed from a certain position
     *
     * @param dice     dice removed
     * @param position removal position
     */
    void onDiceRemove(Dice dice, Position position);
}
