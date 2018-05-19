package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class ModifyDiceValue implements ICommand {

    /**
     * Notify that a new value for a certain dice is needed.
     * Doesn't require anything in toolcard. It requires a dice in toolcard
     * It will modify it's value and puts the new Dice in toolcard.
     *
     * @param player           Player who invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param game             Game in which the player acts
     * @return true
     * @throws RemoteException if there are network communication errors
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, InterruptedException, ExecutionCommandException {
        Dice dice = toolCardExecutor.getNeededDice();
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        for (IToolCardExecutorObserver obs : observerList) {
            obs.notifyNeedNewValue();
        }
        int integer = toolCardExecutor.getNeededValue();
        if(integer < Dice.MIN_VALUE || integer > Dice.MAX_VALUE)
            throw new ExecutionCommandException();
        toolCardExecutor.setNeededDice(new Dice(integer, dice.getColor()));
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof ModifyDiceValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ModifyDiceValue.class);
    }
}
