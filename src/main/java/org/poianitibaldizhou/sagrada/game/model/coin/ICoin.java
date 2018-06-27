package org.poianitibaldizhou.sagrada.game.model.coin;

import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

/**
 * OVERVIEW: Represents the entity of the coin that can be used in the game. This change
 * from single player to multi player
 */
public interface ICoin {

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

