package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.util.Objects;

public class AddDiceToDraftPool implements ICommand {

    /**
     * Add the ToolCard's dice to the DraftPool.
     * This method requires a dice in ToolCard
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutor ToolCard invoked that contains this command
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException given by wait of toolCard.getNeededDice()
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws InterruptedException {
        Dice dice = toolCardExecutor.getNeededDice();
        game.addDiceToDraftPool(dice);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDraftPool;
    }

    @Override
    public int hashCode() {
        return Objects.hash(AddDiceToDraftPool.class);
    }
}
