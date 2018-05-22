package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.exception.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.Serializable;


public interface ICoin extends Serializable {

    /**
     *
     * @param toolCard the card which the player would use
     * @return true if the card can be used, otherwise false
     */
    boolean isCardUsable(ToolCard toolCard);

    int getCoins();

    void removeCoins(int cost);
}

