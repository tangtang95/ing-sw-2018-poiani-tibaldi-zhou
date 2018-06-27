package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Command that will make skip a turn to a certain player
 */
public class SkipTurn implements ICommand {

    private final int turnValue;

    /**
     * Constructor.
     * Creates a skip turn command that will make skip the specified turn to the player
     * who is executing the tool card
     * @param turn turn that will be skipped
     */
    public SkipTurn(int turn) {
        if (turn < TurnState.FIRST_TURN || turn > TurnState.SECOND_TURN)
            throw new IllegalArgumentException("Turn has to be 1 or 2");
        this.turnValue = turn;
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
        toolCardExecutor.addSkipTurnPlayer(player, turnValue);
        return CommandFlow.MAIN;
    }

    /**
     * @return turn that will be skipped
     */
    public int getTurnValue() {
        return turnValue;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SkipTurn))
            return false;

        SkipTurn obj = (SkipTurn) object;
        return obj.getTurnValue() == this.turnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(SkipTurn.class, getTurnValue());
    }
}
