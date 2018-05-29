package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;

import java.io.IOException;
import java.rmi.Remote;
public interface ISchemaCardObserver extends Remote{

    /**
     * Notify that a dice's been placed in a certain position
     *
     * @param dice     dice placed
     * @param position placement position
     */
    void onPlaceDice(Dice dice, Position position) throws IOException;


    /**
     * Notify that a dice's been removed from a certain position
     *
     * @param dice     dice removed
     * @param position removal position
     */
    void onDiceRemove(Dice dice, Position position) throws IOException;
}
