package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;

import java.io.IOException;
import java.rmi.Remote;
import java.util.List;

public interface IRoundTrackObserver extends Remote {

    /**
     * Notify that a list of dices has been added to a certain round.
     *
     * @param diceList list of dices added
     * @param round    the dices have been added to this round
     * @throws IOException network error
     */
    void onDicesAddToRound(List<Dice> diceList, int round) throws IOException;

    /**
     * Notify that a dice has been added to a certain round.
     *
     * @param dice  dice added
     * @param round dice's been added to this round
     * @throws IOException network error
     */
    void onDiceAddToRound(Dice dice, int round) throws IOException;

    /**
     * Notify that a dice's been removed from a certain round
     *
     * @param dice  dice removed
     * @param round dice's been removed in this round
     * @throws IOException network error
     */
    void onDiceRemoveFromRound(Dice dice, int round) throws IOException;

    /**
     * Notify that a dice of a certain round has been removed and that a new dice
     * has been added at its place.
     *
     * @param oldDice dice removed
     * @param newDice dice added
     * @param round   round in which the swap occurs
     * @throws IOException network error
     */
    void onDiceSwap(Dice oldDice, Dice newDice, int round) throws IOException;
}
