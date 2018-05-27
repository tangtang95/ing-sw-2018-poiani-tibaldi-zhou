package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class IfDicePlaceable implements ICommand {

    /**
     * Check if the dice given is positionable on the position given on the schemaCard of the player invoker.
     *
     * @param player           player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param turnState        state in which the player acts
     * @return MAIN flow if the dice is positionable, otherwise SUB flow
     * @throws InterruptedException given by wait of getNeededDice() and getNeededPosition()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws  InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();

        if (!toolCardExecutor.getTemporarySchemaCard().isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)) {
            return CommandFlow.SUB;
        }
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof IfDicePlaceable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(IfDicePlaceable.class);
    }
}
