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
     *
     * @param player player's that used the toolcard
     * @param toolCard toolcard used
     * @param game game in which the player acts
     * @throws InterruptedException error with wait()
     */
    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException {
        Dice chosenDice = toolCard.getNeededDice();

        DraftPool draftPool = game.getDraftPool();
        draftPool.addDice(chosenDice.pourOverDice());
        try {
            draftPool.useDice(chosenDice);
        } catch (DiceNotFoundException e) {
            e.printStackTrace();
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof PourOverDice;
    }
}