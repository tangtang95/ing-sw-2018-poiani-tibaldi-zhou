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
     * @throws NoCoinsExpendableException               if there aren't any expandable favor tokens
     * @throws IllegalNumberOfTokensOnToolCardException if on the ToolCard there is a number of tokens < 0 or 1
     */
    @Override
    public void use(ToolCard toolCard) throws NoCoinsExpendableException, IllegalNumberOfTokensOnToolCardException {
        if (numberOfFavorToken < toolCard.getCost())
            throw new NoCoinsExpendableException("FavorToken.use() failed, you haven't enough FavorTokens");
        else
            this.numberOfFavorToken = numberOfFavorToken - toolCard.getCost();
    }

    @Override
    public int getCoins() {
        return numberOfFavorToken;
    }
}
