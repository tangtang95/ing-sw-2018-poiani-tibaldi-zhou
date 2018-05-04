package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    public FavorToken() {
        this.numberOfFavorToken = 0;
    }

    public FavorToken(int numberOfFavorToken) {
        this.numberOfFavorToken = numberOfFavorToken;
    }

    /**
     * if the number of favor tokens are greater of the cost of toolCard, decrement the player's number of Favor Tokens
     *
     * @param toolCard the card which the player would use
     * @throws NoCoinsExpendableException if there aren't any expandable favor tokens
     */
    @Override
    public void use(ToolCard toolCard) throws NoCoinsExpendableException{
        int cost = 0;
        try {
            cost = toolCard.getCost();
        } catch (IllegalNumberOfTokensOnToolCardException e) {
            e.printStackTrace();
        }
        if (numberOfFavorToken < cost)
            throw new NoCoinsExpendableException("FavorToken.use() failed, you haven't enough FavorTokens");
        else
            this.numberOfFavorToken = numberOfFavorToken - cost;
    }

    @Override
    public int getCoins() {
        return numberOfFavorToken;
    }
}
