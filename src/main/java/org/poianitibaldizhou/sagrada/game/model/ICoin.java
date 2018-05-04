package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;


public interface ICoin {

    /**
     * decrement the expendable coins when the player use that
     *
     * @param toolCard the card which the player would use
     * @throws NoCoinsExpendableException if there aren't any expandable favor tokens or dices
     */
    void use(ToolCard toolCard) throws NoCoinsExpendableException;

    int getCoins();

}