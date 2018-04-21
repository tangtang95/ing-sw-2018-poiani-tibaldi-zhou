package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public interface ICoin {

    void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException, MismatchingTypeOfConstraintException, EmptyCollectionException, IllegalNumberOfTokensOnToolCardException;

    int getCoins();
}