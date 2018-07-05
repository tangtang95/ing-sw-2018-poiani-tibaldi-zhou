package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

import java.util.Objects;

/**
 * OVERVIEW: This is the coin used in the single player version of Sagrada. The coin that
 * are used are the dice present in the draft pool
 */
public class ExpendableDice implements ICoin {

    private final Game game;

    /**
     * Constructor.
     * Creates the coin for a specific game.
     *
     * @param game game in which the expandable dice are used
     */
    public ExpendableDice(Game game) {
        this.game = game;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCardUsable(ToolCard toolCard) {
        DraftPool draftPool = game.getDraftPool();
        final ColorConstraint colorConstraint = new ColorConstraint(toolCard.getColor());
        return draftPool.getDices().stream()
                .map(Dice::getColorConstraint).anyMatch(constraint -> constraint.equals(colorConstraint));
    }

    /**
     * This action is not possible with the expandable dice, therefore this method throws
     * an illegal state exception
     */
    @Override
    public int getCoins() {
        throw new IllegalStateException();
    }

    /**
     * This action is not possible with the expandable dice, therefore this method throws
     * an illegal state exception
     */
    @Override
    public void removeCoins(int cost) {
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExpendableDice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ExpendableDice.class);
    }
}
