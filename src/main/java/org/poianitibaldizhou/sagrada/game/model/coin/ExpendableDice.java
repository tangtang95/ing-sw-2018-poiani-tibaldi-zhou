package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;

import java.rmi.RemoteException;

public class ExpendableDice implements ICoin {

    private final transient Game game;

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
