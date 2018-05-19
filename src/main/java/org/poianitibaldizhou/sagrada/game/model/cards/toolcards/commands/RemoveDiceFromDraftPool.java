package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;
import java.util.Objects;

public class RemoveDiceFromDraftPool implements ICommand {

    /**
     * It requires a dice in the toolcard and removes it from the toolcard.
     *
     * @param player           player who invoked the toolcard
     * @param toolCardExecutor toolcard invoked
     * @param stateGame        state in which the player acts
     * @return CommandFlow.STOP if the dice isn't present in DraftPool, CommandFlow.MAIN otherwise
     * @throws RemoteException      network communication error
     * @throws InterruptedException due to wait()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) throws RemoteException, InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        try {
            toolCardExecutor.getTemporaryDraftpool().useDice(dice);
        } catch (DiceNotFoundException | EmptyCollectionException e) {
            return CommandFlow.STOP;
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RemoveDiceFromDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveDiceFromDraftPool.class);
    }
}
