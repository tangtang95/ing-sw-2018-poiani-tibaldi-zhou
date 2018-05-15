package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;

public class ModifyDiceValueByDelta implements ICommand {

    private final int value;

    public ModifyDiceValueByDelta(int value) {
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
     * @param player Player who invoked toolcard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param game Game in which the player acts
     * @throws RemoteException network communication error
     * @throws InterruptedException due to the wait() in  toolCard.getDice()
     * @return true if methods execute correctly, false if the new value doesn't respect the rules.
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, InterruptedException, ExecutionCommandException {
        Dice dice = toolCardExecutor.getNeededDice();
        toolCardExecutor.setNeededDice(dice);

        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        for(IToolCardExecutorObserver obs : observerList)
            obs.notifyNeedNewDeltaForDice(dice.getNumber(), value);

        int newValue = toolCardExecutor.getNeededValue();

        if(!checkNewValueValidity(newValue, dice.getNumber())) {
            throw new ExecutionCommandException();
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
     * Also, new value must be in range [Dice.MAX_VALUE, Dice.MIN_VALUE]
     *
     * @param newValue new value
     * @param oldValue old value
     * @return true if new value is valid, false otherwise
     */
    @Contract(pure = true)
    private boolean checkNewValueValidity(int newValue, int oldValue) {
        if(newValue < 1 || newValue > 6)
            return false;
        return (newValue == oldValue + this.value) || (newValue == oldValue - this.value);
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof ModifyDiceValueByDelta))
            return false;

        ModifyDiceValueByDelta obj = (ModifyDiceValueByDelta) object;
        return this.value == obj.getValue();
    }
}
