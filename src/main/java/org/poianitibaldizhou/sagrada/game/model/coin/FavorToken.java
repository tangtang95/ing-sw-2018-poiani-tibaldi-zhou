package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    public FavorToken(int numberOfFavorToken) {
        this.numberOfFavorToken = numberOfFavorToken;
    }

    /**
     * if the number of favor tokens are greater of the cost of toolCard, decrement the player's number of Favor Tokens
     *
     * @param toolCard the card which the player would use
     */
    @Override
    public boolean isCardUsable(ToolCard toolCard) {
        return numberOfFavorToken >= toolCard.getCost();
    }

    @Override
    public int getCoins() {
        return numberOfFavorToken;
    }

    @Override
    public void removeCoins(int cost) {
        if(numberOfFavorToken < cost)
            throw new IllegalArgumentException();
        numberOfFavorToken = numberOfFavorToken - cost;
    }
}
