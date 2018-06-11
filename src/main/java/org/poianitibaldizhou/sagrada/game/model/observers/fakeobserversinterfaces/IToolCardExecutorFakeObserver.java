package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;

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
     * @param dice the dice to change the value
     */
    void notifyNeedNewValue(Dice dice);

    /**
     * Notify the requirement of a color
     *
     * @param colors the list of colors from where to choose the color
     */
    void notifyNeedColor(Set<Color> colors);

    /**
     * Notify the requirement of a new value for a dice that need to respect the delta variation
     *  @param dice the dice number to change
     * @param value     the delta variation to apply (increment or decrement)
     */
    void notifyNeedNewDeltaForDice(Dice dice, int value);

    /**
     * Notify the requirement of a dice from the roundTrack
     *
     * @param roundTrack the roundTrack of the game
     */
    void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack);

    /**
     * Notify the requirement of a position on the schemaCard
     *
     * @param schemaCard schema card on which to choose the position
     */
    void notifyNeedPositionForRemoving(SchemaCard schemaCard);

    /**
     * Notify the requirement of a position of a dice on schemaCard of a certain color
     *
     * @param schemaCard schema card on which to choose the position
     * @param color      the certain color
     */
    void notifyNeedDicePositionOfCertainColor(Color color, SchemaCard schemaCard);

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

    /**
     * Notifies that a dice has been re-rolled.
     *
     * @param dice the dice after the re-roll
     */
    void notifyDiceReroll(Dice dice);

    /**
     * Notify a dice that has been poured over
     *
     * @param dice poured over dice
     */
    void notifyDicePouredOver(Dice dice);

    /**
     * Notify that the execution of the ToolCard is ended
     */
    void notifyExecutionEnded();

    /**
     * Notify that the ToolCard is waiting for TurnEnd
     */
    void notifyWaitTurnEnd();

    /**
     * Notify the requirement of a position on the schemaCard for placing a dice
     *
     * @param schemaCard schema card on which to choose the position
     * @param dice the dice to be placed
     */
    void notifyNeedPositionForPlacement(SchemaCard schemaCard, Dice dice);
}