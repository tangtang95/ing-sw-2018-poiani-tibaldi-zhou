package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Remove the favor token for the usage of the tool card
 */
public class RemoveFavorToken implements ICommand {

    private int numberOfTokenToRemove;

    /**
     * Removing a certain number of favor token depending on the tool card cost
     *
     * @param cost favor token that will be removed
     */
    public RemoveFavorToken(int cost) {
        numberOfTokenToRemove = cost;
    }

    /**
     * This command removes the number of favor token from player for the usage of the tool card
     *
     * @param player           players that executed the command
     * @param toolCardExecutor tool card executor that executed this command
     * @param turnState        turn in which the execution happens
     * @return CommandFlow.MAIN
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        player.removeCoins(numberOfTokenToRemove);
        return CommandFlow.MAIN;
    }

    @Contract(pure = true)
    public int getNumberOfTokenToRemove() {
        return numberOfTokenToRemove;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RemoveFavorToken && numberOfTokenToRemove == ((RemoveFavorToken) obj).numberOfTokenToRemove;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveFavorToken.class, numberOfTokenToRemove);
    }
}
