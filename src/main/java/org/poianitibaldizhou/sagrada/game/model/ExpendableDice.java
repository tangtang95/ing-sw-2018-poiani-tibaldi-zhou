package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public class ExpendableDice implements ICoin {
    private DraftPool draftPool;

    public ExpendableDice(DraftPool draftPool) {
        this.draftPool = draftPool;
    }

    @Override
    public void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException, EmptyCollectionException {
        draftPool.useDice(searchDice(toolCard.getColor()));
    }

    @Override
    public int getCoins() {
        return draftPool.getDices().size();
    }

    private Dice searchDice(Color color) throws NoCoinsExpendableException{
        ColorConstraint colorConstraint = new ColorConstraint(color);
        for (Dice dice: draftPool.getDices()) {
            if (colorConstraint.matches(dice.getColorConstraint()))
                return dice;
        }
        throw new NoCoinsExpendableException("ExpendableDice.use() failed, there aren't dices to spend");
    }
}
