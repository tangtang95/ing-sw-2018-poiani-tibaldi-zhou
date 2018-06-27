package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;

/**
 * OVERVIEW: Fake observer for a certain schema card.
 * Fake observers are observer present on the server that listen to changes and modifications.
 * In this way, the network part is decoupled from the model.
 */
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