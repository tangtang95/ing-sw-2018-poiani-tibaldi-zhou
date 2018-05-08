package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class PourOverDice implements ICommand {

    /**
     * Pour overs a certain dice in the draftpool.
     * Needs a dice to pour over in toolcard, it pours it over and sets it to toolcard.
     *
     * @param player player's that used the toolcard
     * @param toolCard toolcard used
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException error with wait()
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException {
        Dice chosenDice = toolCard.getNeededDice();
        toolCard.setNeededDice(chosenDice.pourOverDice());
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PourOverDice;
    }
}