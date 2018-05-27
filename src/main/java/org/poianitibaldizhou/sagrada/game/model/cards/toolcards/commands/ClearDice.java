package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class ClearDice implements ICommand {

    /**
     * Clear the dice parameter in the executor.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutor executor of the toolcard
     * @param turnState state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        toolCardExecutor.setNeededDice(null);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClearDice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClearDice.class);
    }
}
