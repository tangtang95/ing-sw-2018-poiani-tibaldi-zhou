package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Represents the command of checking that the tool card is being executed
 * in a specific turn.
 */
public class CheckTurn implements ICommand {
    private final int turn;

    /**
     * Constructor.
     * Creates a check turn command regarding a certain turn
     * @param turn turn to check
     */
    public CheckTurn(int turn) {
        if(turn < 1 || turn > 2)
            throw new IllegalArgumentException("illegal value of turn");
        this.turn = turn;
    }

    /**
     * Check if the turn in which the player acts is the same specified by this command.
     * If it's not, then the process of the commands must terminate, otherwise proceeds.
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor executor that processes the commands
     * @param turnState state in which the player acts
     * @return CommandFlow.MAIN if the turn is correct, CommandFlow.TURN_CHECK_FAILED otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        if(turnState.isFirstTurn() != (getTurn() == 1)) {
            return CommandFlow.TURN_CHECK_FAILED;
        }
        return CommandFlow.MAIN;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof CheckTurn))
            return false;
        CheckTurn obj = (CheckTurn) object;
        return this.turn == obj.getTurn();
    }

    @Override
    public int hashCode() {
        return Objects.hash(CheckTurn.class, turn);
    }
}