package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class AddDiceToDiceBag implements ICommand {

    /**
     * Adds the toolCard's dice to game's DiceBag.
     *
     * @param player Player who invoked the command
     * @param toolCard toolCard that has been invoked and that contains this command
     * @param game Game in which the player acts
     * @throws InterruptedException due to the wait for toolCard.getNeededDice()
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException {
        Dice dice = toolCard.getNeededDice();
        DrawableCollection<Dice> dicebag = game.getDiceBag();
        dicebag.addElement(dice);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof AddDiceToDiceBag;
    }
}
