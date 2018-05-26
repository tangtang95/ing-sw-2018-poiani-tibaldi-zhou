package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface IToolCardExecutorObserver extends Remote {
    /**
     * Notify the requirement of a dice
     *
     * @param diceList the list of dices from where to choose the dice
     * @throws RemoteException network error
     */
    void notifyNeedDice(List<Dice> diceList) throws RemoteException;

    /**
     * Notify the requirement of a value (from an interval of number)
     * //TODO pass a interval to the function (?)
     *
     * @throws RemoteException network error
     */
    void notifyNeedNewValue() throws RemoteException;

    /**
     * Notify the requirement of a color
     *
     * @param colors the list of colors from where to choose the color
     * @throws RemoteException network error
     */
    void notifyNeedColor(Set<Color> colors) throws RemoteException;

    /**
     * Notify the requirement of a new value for a dice that need to respect the delta variation
     *
     * @param diceValue the dice number to change
     * @param value the delta variation to apply (increment or decrement)
     * @throws RemoteException network error
     */
    void notifyNeedNewDeltaForDice(int diceValue, int value) throws RemoteException;

    /**
     * Notify the requirement of a dice from the roundTrack
     *
     * @param roundTrack the roundTrack of the game
     * @throws RemoteException network error
     */
    void notifyNeedDiceFromRoundTrack(RoundTrack roundTrack) throws RemoteException;

    /**
     * Notify the requirement of a position on the schemaCard
     *
     * @throws RemoteException network error
     */
    void notifyNeedPosition() throws RemoteException;

    /**
     * Notify the requirement of a position of a dice on schemaCard of a certain color
     *
     * @param color the certain color
     * @throws RemoteException network error
     */
    void notifyNeedDicePositionOfCertainColor(Color color) throws RemoteException;

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
}
