package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;

public interface ISchemaCardFakeObserver {

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
    void onDiceRemove(Dice dice, Position position) ;
}