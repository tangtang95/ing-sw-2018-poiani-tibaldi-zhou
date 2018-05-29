package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;

import java.io.IOException;
import java.rmi.Remote;
import java.util.List;
import java.util.Set;

public interface IToolCardExecutorObserver extends Remote {
    /**
     * Notify the requirement of a dice
     *
     * @param diceList the list of dices from where to choose the dice
     * @throws IOException network error
     */
    void notifyNeedDice(List<Dice> diceList) throws IOException;

    /**
     * Notify the requirement of a value (from an interval of number)
     * //TODO pass a interval to the function (?)
     *
     * @throws IOException network error
     */
    void notifyNeedNewValue() throws IOException;

    /**
     * Notify the requirement of a color
     *
     * @param colors the list of colors from where to choose the color
     * @throws IOException network error
     */
    void notifyNeedColor(Set<Color> colors) throws IOException;

    /**
     * Notify the requirement of a new value for a dice that need to respect the delta variation
     *
     * @param diceValue the dice number to change
     * @param value the delta variation to apply (increment or decrement)
     * @throws IOException network error
     */
    void notifyNeedNewDeltaForDice(int diceValue, int value) throws IOException;

    /**
     * Notify the requirement of a dice from the roundTrack
     *
     * @param roundTrack the roundTrack of the game
     * @throws IOException network error
     */
    void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws IOException;

    /**
     * Notify the requirement of a position on the schemaCard
     *
     * @throws IOException network error
     */
    void notifyNeedPosition() throws IOException;

    /**
     * Notify the requirement of a position of a dice on schemaCard of a certain color
     *
     * @param color the certain color
     * @throws IOException network error
     */
    void notifyNeedDicePositionOfCertainColor(Color color) throws IOException;

    /**
     * Notify the necessity to repeat the action (because of a failed command)
     *
     * @throws IOException network error
     */
    void notifyRepeatAction() throws IOException;

    /**
     * Notify the interruption of the command because of the CommandFlow error
     *
     * @param error the cause of the interruption
     * @throws IOException network error
     */
    void notifyCommandInterrupted(CommandFlow error) throws IOException;

    /**
     * Notify to the client the requirement of an answer to the continuation of the tool card execution
     *
     * @throws IOException network error
     */
    void notifyNeedContinueAnswer() throws IOException;
}