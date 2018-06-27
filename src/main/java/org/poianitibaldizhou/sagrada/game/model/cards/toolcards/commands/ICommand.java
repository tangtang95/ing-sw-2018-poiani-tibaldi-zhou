package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

/**
 * OVERVIEW: Represents a command that can be part of the complex action of a tool card.
 */
public interface ICommand {

    /**
     * Execute the command.
     *
     * @param player           players that executed the command
     * @param toolCardExecutor tool card executor that executed this command
     * @param turnState        turn in which the execution happens
     * @return the flow of the commands in the execution tree of the tool card commands
     * @throws InterruptedException due to wait calls that are necessary for
     */
    CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException;
}

