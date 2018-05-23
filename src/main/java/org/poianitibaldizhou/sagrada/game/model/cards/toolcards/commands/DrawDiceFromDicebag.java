package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

public class DrawDiceFromDicebag implements ICommand {

    /**
     * Draws a dice from the DiceBag.
     * Doesn't require anything in toolCard.
     * It pushes a dice to toolCard.
     *
     * @param player           player who invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.EMPTY_DICEBAG in the DiceBag is empty, CommandFlow.MAIN otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        Dice dice;
        try {
            dice = toolCardExecutor.getTemporaryDicebag().draw();
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DICEBAG;
        }
        toolCardExecutor.setNeededDice(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DrawDiceFromDicebag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(DrawDiceFromDicebag.class);
    }
}
