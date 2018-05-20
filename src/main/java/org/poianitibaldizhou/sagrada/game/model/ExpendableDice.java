package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

public class ExpendableDice implements ICoin {

    private final Game game;

    public ExpendableDice(Game game) {
        this.game = game;
    }

    /**
     * if there's a dice expendable for the ToolCard is deleted from the DraftPoll
     *
     * @param toolCard the card which the player would use
     */
    @Override
    public boolean isCardUsable(ToolCard toolCard) {
        DraftPool draftPool = game.getDraftPool();
        final ColorConstraint colorConstraint = new ColorConstraint(toolCard.getColor());
        return draftPool.getDices().stream()
                .map(Dice::getColorConstraint).filter(constraint -> constraint.equals(colorConstraint)).count() > 0;
    }

    @Override
    public int getCoins() {
        throw new IllegalStateException();
    }

    @Override
    public void removeCoins(int cost) {
        throw new IllegalStateException();
    }
}
