package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Command necessary for clearing the value regarding the condition regarding
 * the end turn in the tool card executor
 */
public class ClearTurnEndCondition implements ICommand {

    /**
     * Set to false the turn end of the executor.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutor executor of the toolcard
     * @param turnState state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        toolCardExecutor.setTurnEnded(false);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        return o instanceof ClearTurnEndCondition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClearTurnEndCondition.class);
    }
}
