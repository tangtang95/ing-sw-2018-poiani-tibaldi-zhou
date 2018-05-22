package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

import java.util.Objects;

public class CheckBeforeDiceChosen implements ICommand {

    /**
     * Check if the player has already placed a dice or not in the current turn.
     *
     * @param player           player that invoked the ToolCard
     * @param toolCardExecutor executor of the Toolcard that contains this command
     * @param turnState        state
     * @return the MAIN flow if the dice hasn't been chosen and placed yet in the current turn,
     * DICE_ALREADY_PLACED flow otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        if (turnState.hasActionUsed(new PlaceDiceAction()))
            return CommandFlow.DICE_ALREADY_PLACED;
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof CheckBeforeDiceChosen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(CheckBeforeDiceChosen.class);
    }
}
