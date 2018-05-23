package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;


public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    public FavorToken(int numberOfFavorToken) {
        this.numberOfFavorToken = numberOfFavorToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCardUsable(ToolCard toolCard) {
        return numberOfFavorToken >= toolCard.getCost();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCoins() {
        return numberOfFavorToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCoins(int cost) {
        if(numberOfFavorToken < cost)
            throw new IllegalArgumentException();
        numberOfFavorToken = numberOfFavorToken - cost;
    }
}
