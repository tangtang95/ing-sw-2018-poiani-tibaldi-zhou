package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.IllegalNumberOfTokensOnToolCardException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    public FavorToken(int numberOfFavorToken) {
        this.numberOfFavorToken = numberOfFavorToken;
    }

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
