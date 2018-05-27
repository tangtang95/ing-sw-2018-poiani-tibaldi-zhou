package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class ClearAll implements ICommand {

    /**
     * Clear the whole parameters in the executor.
     *
     * @param player player who invoked the toolcard
     * @param toolCardExecutor executor of the toolcard that need to be cleaned
     * @param turnState state in which the card is executed
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        toolCardExecutor.setNeededDice(null);
        toolCardExecutor.setNeededPosition(null);
        toolCardExecutor.setTurnEnded(false);
        toolCardExecutor.setNeededColor(null);
        toolCardExecutor.setNeededValue(null);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClearAll;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClearAll.class);
    }
}
