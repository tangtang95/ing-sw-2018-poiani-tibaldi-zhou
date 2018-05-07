package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.Random;

public class RerollDice implements ICommand {

    /**
     * Re-roll ToolCard's dice and set it to the ToolCard again for further uses.
     *
     * @param player player that invoked the ToolCard
     * @param toolCard ToolCard that contains this command
     * @param game Game in which player acts
     * @throws InterruptedException
     */
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException {
        Dice dice = toolCard.getNeededDice();
        Random rand = new Random();
        dice = new Dice(rand.nextInt(Dice.MAX_VALUE-1)+1, dice.getColor());
        toolCard.setNeededDice(dice);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDice;
    }
}
