package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

public class AddDiceToDraftPool implements ICommand {

    /**
     * Add the ToolCard's dice to the DraftPool.
     * This method requires a dice in ToolCard
     *
     * @param player player that invoked the ToolCard
     * @param toolCardExecutorHelper ToolCard invoked that contains this command
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException given by wait of toolCard.getNeededDice()
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws InterruptedException {
        Dice dice = toolCardExecutorHelper.getNeededDice();
        game.addDiceToDraftPool(dice);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDraftPool;
    }
}
