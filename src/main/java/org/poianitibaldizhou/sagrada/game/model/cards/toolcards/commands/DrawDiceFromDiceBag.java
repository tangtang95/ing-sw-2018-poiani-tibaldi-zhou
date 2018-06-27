package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.Objects;

/**
 * OVERVIEW: Command necessary for drawing a dice from the dice bag
 */
public class DrawDiceFromDiceBag implements ICommand {

    /**
     * Draws a dice from the DiceBag.
     * Doesn't require anything in toolCard.
     * It pushes a dice to toolCard.
     *
     * @param player           player who invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param turnState        state in which the player acts
     * @return CommandFlow.EMPTY_DICE_BAG in the DiceBag is empty, CommandFlow.MAIN otherwise
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) {
        Dice dice;
        try {
            dice = toolCardExecutor.getTemporaryDiceBag().draw();
        } catch (EmptyCollectionException e) {
            return CommandFlow.EMPTY_DICE_BAG;
        }
        toolCardExecutor.setNeededDice(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof DrawDiceFromDiceBag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(DrawDiceFromDiceBag.class);
    }
}
