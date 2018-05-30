package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface IToolCardExecutorFakeObserver {

    /**
     * Notify the requirement of a dice
     *
     * @param diceList the list of dices from where to choose the dice
     */
    void notifyNeedDice(List<Dice> diceList);

    /**
     * Notify the requirement of a value (from an interval of number)
     * //TODO pass a interval to the function (?)
     */
    void notifyNeedNewValue();

    /**
     * Notify the requirement of a color
     *
     * @param colors the list of colors from where to choose the color
     */
    void notifyNeedColor(Set<Color> colors);

    /**
     * Notify the requirement of a new value for a dice that need to respect the delta variation
     *
     * @param diceValue the dice number to change
     * @param value     the delta variation to apply (increment or decrement)
     */
    void notifyNeedNewDeltaForDice(int diceValue, int value);

    /**
     * Notify the requirement of a dice from the roundTrack
     *
     * @param roundTrack the roundTrack of the game
     */
    void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack);

    /**
     * Notify the requirement of a position on the schemaCard
     */
    void notifyNeedPosition();

    /**
     * Notify the requirement of a position of a dice on schemaCard of a certain color
     *
     * @param color the certain color
     */
    void notifyNeedDicePositionOfCertainColor(Color color);

    /**
     * Notify the necessity to repeat the action (because of a failed command)
     */
    void notifyRepeatAction();

    /**
     * Notify the interruption of the command because of the CommandFlow error
     *
     * @param error the cause of the interruption
     */
    void notifyCommandInterrupted(CommandFlow error);

    /**
     * Notify to the client the requirement of an answer to the continuation of the tool card execution
     */
    void notifyNeedContinueAnswer();
}