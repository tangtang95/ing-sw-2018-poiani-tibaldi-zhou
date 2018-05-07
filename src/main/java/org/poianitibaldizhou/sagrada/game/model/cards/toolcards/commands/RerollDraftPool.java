package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.List;
import java.util.Random;

public class RerollDraftPool implements ICommand {

    /**
     * Re-roll every dice presents in the DraftPool
     *  @param player player that invoked the ToolCard containing this command
     * @param toolCard ToolCard that used this command
     * @param game game in which the player acts
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) {
        Random rand = new Random();
        DraftPool draftPool = game.getDraftPool();
        List<Dice> draftPoolDices = game.getDraftPool().getDices();
        Dice temp;
        for(Dice d: draftPoolDices) {
            draftPool.addDice(new Dice(rand.nextInt(Dice.MAX_VALUE-1)+1, d.getColor()));
            try {
                draftPool.useDice(d);
            } catch (DiceNotFoundException  | EmptyCollectionException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof RerollDraftPool;
    }
}
