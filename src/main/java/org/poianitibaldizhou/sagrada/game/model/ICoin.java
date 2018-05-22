package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;


public interface ICoin {

    /**
     *
     * @param toolCard the card which the player would use
     * @return true if the card can be used, otherwise false
     */
    boolean isCardUsable(ToolCard toolCard);

    int getCoins();

    void removeCoins(int cost);
}

