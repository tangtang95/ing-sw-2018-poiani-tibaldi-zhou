package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;


public interface ICoin {

    void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException, EmptyCollectionException, IllegalNumberOfTokensOnToolCardException;

    int getCoins();
}