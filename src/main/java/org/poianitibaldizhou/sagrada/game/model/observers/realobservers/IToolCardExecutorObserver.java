package org.poianitibaldizhou.sagrada.game.model.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

public interface IToolCardExecutorObserver extends Remote {
    /**
     * Notify the requirement of a dice
     *
     * @param diceList the list of dices from where to choose the dice
     * @throws IOException network error
     */
    void notifyNeedDice(String diceList) throws IOException;

    /**
     * Notify the requirement of a value (from an interval of number)
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
    void notifyNeedColor(String colors) throws IOException;

    /**
     * Notify the requirement of a new value for a dice that need to respect the delta variation
     *
     * @param message message containing dice value an the delta
     * @throws IOException network error
     */
    void notifyNeedNewDeltaForDice(String message) throws IOException;

    /**
     * Notify the requirement of a dice from the roundTrack
     *
     * @param roundTrack the roundTrack of the game
     * @throws IOException network error
     */
    void notifyNeedDiceFromRoundTrack(String roundTrack) throws IOException;

    /**
     * Notify the requirement of a position on the schemaCard
     *
     * @message protocol message containing the schemacard
     * @throws IOException network error
     */
    void notifyNeedPosition(String message) throws IOException;

    /**
     * Notify the requirement of a position of a dice on schemaCard of a certain color
     *
     * @param message protocol message containing the color and the schema card
     * @throws IOException network error
     */
    void notifyNeedDicePositionOfCertainColor(String message) throws IOException;

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
    void notifyCommandInterrupted(String error) throws IOException;

    /**
     * Notify to the client the requirement of an answer to the continuation of the tool card execution
     *
     * @throws IOException network error
     */
    void notifyNeedContinueAnswer() throws IOException;

    /**
     * Notify that a dice has been re-rolled.
     *
     * @param message protocol message containing the re-rolled dice
     * @throws IOException network error
     */
    void notifyDiceReroll(String message) throws IOException;

    /**
     * Notify that the execution of the ToolCard is ended
     */
    void notifyExecutionEnded() throws IOException;
}
