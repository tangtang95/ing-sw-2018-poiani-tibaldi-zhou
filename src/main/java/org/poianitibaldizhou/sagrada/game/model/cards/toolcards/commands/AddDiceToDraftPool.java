package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class AddDiceToDraftPool implements ICommand {

    /**
     * Add the ToolCard's dice to the DraftPool.
     *
     * @param player player that invoked the ToolCard
     * @param toolCard ToolCard invoked that contains this command
     * @param game game in which the player acts
     * @throws InterruptedException given by wait of toolCard.getNeededDice()
     */
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException {
        Dice dice = toolCard.getNeededDice();
        game.getDraftPool().addDice(dice);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDraftPool;
    }
}
