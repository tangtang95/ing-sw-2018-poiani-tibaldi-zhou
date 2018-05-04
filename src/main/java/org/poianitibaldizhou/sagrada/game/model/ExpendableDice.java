package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

public class ExpendableDice implements ICoin {
    private DraftPool draftPool;

    public ExpendableDice(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    /**
     * if there's a dice expendable for the ToolCard is deleted from the DraftPoll
     *
     * @param toolCard the card which the player would use
     * @throws NoCoinsExpendableException if there aren't any expandable dice in the DraftPool
     */
    @Override
    public void use(ToolCard toolCard) throws NoCoinsExpendableException{
        try {
            draftPool.useDice(searchDice(toolCard.getColor()));
        } catch (DiceNotFoundException e) {
            e.printStackTrace();
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCoins() {
        return draftPool.getDices().size();
    }


    /**
     * search a dice in the DraftPool with the color chosen
     *
     * @param color color of the chosen dice
     * @return a dice with the color chosen from the DraftPoll
     * @throws NoCoinsExpendableException if there aren't any expandable dice in the DraftPool
     */
    private Dice searchDice(Color color) throws NoCoinsExpendableException{
        ColorConstraint colorConstraint = new ColorConstraint(color);
        for (Dice dice: draftPool.getDices()) {
            if (colorConstraint.matches(dice.getColorConstraint()))
                return dice;
        }
        throw new NoCoinsExpendableException("ExpendableDice.use() failed, there aren't dices to spend");
    }
}
