package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.players.Outcome;

/**
 * OVERVIEW: Fake observer for the player.
 * Fake observers are observer present on the server that listen to changes and modifications.
 * In this way, the network part is decoupled from the model.
 */
public interface IPlayerFakeObserver {

    /**
     * Notify that the tokens of a certain player has changed
     *
     * @param value the cost variation
     */
    void onFavorTokenChange(int value) ;

    /**
     * Notify the change of outcome of the game to the player
     *
     * @param outcome new outcome of the player
     */
    void onSetOutcome(Outcome outcome) ;
}