package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Command necessary for clearing the value regarding the integer in the tool card
 * executor
 */
public class ClearValue implements ICommand {

    /**
     * Clear the value parameter of the executor.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutor executor of the toolcard
     * @param turnState state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        toolCardExecutor.setNeededValue(null);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClearValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClearValue.class);
    }
}
