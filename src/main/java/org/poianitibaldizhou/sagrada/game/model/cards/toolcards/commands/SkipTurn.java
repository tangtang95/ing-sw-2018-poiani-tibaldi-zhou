package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class SkipTurn implements ICommand {

    private final int value;

    public SkipTurn(int i) {
        if (i != 1 && i != 2)
            throw new IllegalArgumentException();
        this.value = i;
    }

    /**
     * Add skip turn to the player that invoked the ToolCard
     *
     * @param player           player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        turnState.addSkipTurnPlayer(player, value);
        return CommandFlow.MAIN;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SkipTurn))
            return false;

        SkipTurn obj = (SkipTurn) object;
        return obj.getValue() == this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SkipTurn.class, getValue());
    }
}
