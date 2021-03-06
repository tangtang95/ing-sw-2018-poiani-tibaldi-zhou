package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.util.List;

/**
 * OVERVIEW: Fake observer for the round track.
 * Fake observers are observer present on the server that listen to changes and modifications.
 * In this way, the network part is decoupled from the model.
 */
public interface IRoundTrackFakeObserver {

    /**
     * Notify that a list of dices has been added to a certain round.
     *
     * @param diceList list of dices added
     * @param round    the dices have been added to this round
     */
    void onDicesAddToRound(List<Dice> diceList, int round);

    /**
     * Notify that a dice has been added to a certain round.
     *
     * @param dice  dice added
     * @param round dice's been added to this round
     */
    void onDiceAddToRound(Dice dice, int round);

    /**
     * Notify that a dice's been removed from a certain round
     *
     * @param dice  dice removed
     * @param round dice's been removed in this round
     */
    void onDiceRemoveFromRound(Dice dice, int round);

    /**
     * Notify that a dice of a certain round has been removed and that a new dice
     * has been added at its place.
     *
     * @param oldDice dice removed
     * @param newDice dice added
     * @param round   round in which the swap occurs
     */
    void onDiceSwap(Dice oldDice, Dice newDice, int round);
}