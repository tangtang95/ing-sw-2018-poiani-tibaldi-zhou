package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.Serializable;


public interface ICoin extends Serializable {

    /**
     * Return if the toolCard passed is usable or not
     *
     * @param toolCard the card which the player want to use
     * @return true if the toolCard is usable, false otherwise
     */
    boolean isCardUsable(ToolCard toolCard);

    /**
     * @return the coin expendable by the player
     */
    int getCoins();

    /**
     * Remove the coin expandable by the player
     *
     * @param cost the number of the cost
     */
    void removeCoins(int cost);
}

