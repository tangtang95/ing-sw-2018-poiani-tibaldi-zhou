package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.Objects;


/**
 * OVERVIEW: Coins used in the multi player game. They are just an integer value.
 */
public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    /**
     * Constructor.
     * Creates the coin for a certain purpose, initialized at the specified value.
     * @param numberOfFavorToken number of initial coin
     */
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FavorToken && numberOfFavorToken == ((FavorToken) obj).numberOfFavorToken;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FavorToken.class, numberOfFavorToken);
    }
}
