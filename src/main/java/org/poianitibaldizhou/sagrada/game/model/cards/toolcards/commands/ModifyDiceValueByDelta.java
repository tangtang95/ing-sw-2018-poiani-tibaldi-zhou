package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.List;
import java.util.Objects;

/**
 * OVERVIEW: Command that modify the dice value of a certain and specified delta
 */
public class ModifyDiceValueByDelta implements ICommand {

    private final int value;

    /**
     * Constructor.
     * Create a command to modify the dice value by a certain delta (increment or decrement)
     *
     * @param value the delta value (must be greater than 0, and less then 6)
     */
    public ModifyDiceValueByDelta(int value) {
        if (value <= 0 || value >= Dice.MAX_VALUE)
            throw new IllegalArgumentException("value attribute has to be greater than 0, " +
                    "has not to exceed " + (Dice.MAX_VALUE - 1));
        this.value = value;
    }

    /**
     * Notify that a player needs to modify the value of a certain dice by a certain quantity.
     * This method will get the dice from the toolcard, and modify it with the new value.
     * This method will need a dice in toolcard, and it will substitute it with the dice
     * created with the new value.
     * The value doesn't have to be present before the method invocation: it's requested inside
     * the method.
     *
     * @param player           Player who invoked toolcard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN if methods execute correctly, CommandFlow.REPEAT if the new value doesn't respect the rules.
     * @throws InterruptedException due to the wait() in  toolCard.getDice()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();

        List<IToolCardExecutorFakeObserver> observerList = toolCardExecutor.getObservers();
        observerList.forEach(obs -> obs.notifyNeedNewDeltaForDice(dice, getValue()));

        int newValue = toolCardExecutor.getNeededValue();

        if (!checkNewValueValidity(newValue, dice.getNumber())) {
            return CommandFlow.REPEAT;
        }
        toolCardExecutor.setNeededDice(new Dice(newValue, dice.getColor()));
        return CommandFlow.MAIN;
    }

    public int getValue() {
        return value;
    }

    /**
     * Returns true if the new value is valid, false otherwise.
     * For being valid, the new number needs to differentiate from oldValue exactly of
     * this.value.
     * Also, new value must be in range [Dice.MAX_VALUE, Dice.MIN_VALUE].
     *
     * @param newValue new value
     * @param oldValue old value
     * @return true if new value is valid, false otherwise
     */
    @Contract(pure = true)
    private boolean checkNewValueValidity(int newValue, int oldValue) {
        return newValue >= Dice.MIN_VALUE && newValue <= Dice.MAX_VALUE && ((newValue == oldValue + this.value) || (newValue == oldValue - this.value));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ModifyDiceValueByDelta))
            return false;

        ModifyDiceValueByDelta obj = (ModifyDiceValueByDelta) object;
        return this.value == obj.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(ModifyDiceValueByDelta.class, value);
    }
}
