package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;

public class ClearDice implements ICommand {

    /**
     * Clear the dice parameter in the executor.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutor executor of the toolcard
     * @param stateGame state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) {
        toolCardExecutor.setNeededDice(null);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClearDice;
    }
}
