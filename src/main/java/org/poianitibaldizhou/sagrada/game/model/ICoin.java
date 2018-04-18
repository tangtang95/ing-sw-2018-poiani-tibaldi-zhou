package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public interface ICoin {
    void upDate(DraftPool draftPool);
    void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException;
    int getCoins();
}
