package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.card.ToolCard;

public class FavorToken implements ICoin {
    private int numberOfFavorToken;

    public FavorToken(int numberOfFavorToken) {
        this.numberOfFavorToken = numberOfFavorToken;
    }

    @Override
    public void upDate() {

    }

    @Override
    public void use(ToolCard toolCard) {
        this.numberOfFavorToken = numberOfFavorToken - toolCard.getTokens();
    }

    @Override
    public boolean checkCoin(ToolCard toolCard) {
        if (numberOfFavorToken >= toolCard.getTokens() )
            return true;
        else
            return false;
    }
}
