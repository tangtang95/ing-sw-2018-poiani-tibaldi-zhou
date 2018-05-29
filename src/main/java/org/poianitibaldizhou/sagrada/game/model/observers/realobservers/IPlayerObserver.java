package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import org.poianitibaldizhou.sagrada.game.model.players.Outcome;

import java.io.IOException;
import java.rmi.Remote;

public interface IPlayerObserver extends Remote{

    /**
     * Notify that the tokens of a certain player has changed
     *
     * @param value the cost variation
     * @throws IOException network error
     */
    void onFavorTokenChange(int value) throws IOException;

    /**
     * Notify the change of outcome of the game to the player
     *
     * @param outcome new outcome of the player
     * @throws IOException network error
     */
    void onSetOutcome(Outcome outcome) throws IOException;
}
