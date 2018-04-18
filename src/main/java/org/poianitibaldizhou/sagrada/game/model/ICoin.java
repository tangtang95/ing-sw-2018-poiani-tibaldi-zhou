package org.poianitibaldizhou.sagrada.game.model;


import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.NoCoinsExpendableException;
import org.poianitibaldizhou.sagrada.game.model.card.toolcards.ToolCard;

public interface ICoin {

    void use(ToolCard toolCard) throws NoCoinsExpendableException, DiceNotFoundException, EmptyCollectionException;

    int getCoins();
}