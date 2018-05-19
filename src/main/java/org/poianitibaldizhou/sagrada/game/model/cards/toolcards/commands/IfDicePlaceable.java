package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;
import java.util.Objects;

public class IfDicePlaceable implements ICommand {

    /**
     * Check if the dice given is positionable on the position given on the schemaCard of the player invoker
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor executorHelper that contains this command
     * @param stateGame
     * @return MAIN flow if the dice is positionable, otherwise SUB flow
     * @throws InterruptedException given by wait of getNeededDice() and getPosition()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) throws  InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        Position position = toolCardExecutor.getPosition();
        if(!toolCardExecutor.getTemporarySchemaCard().isDicePositionable(dice, position.getRow(), position.getColumn())){
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
