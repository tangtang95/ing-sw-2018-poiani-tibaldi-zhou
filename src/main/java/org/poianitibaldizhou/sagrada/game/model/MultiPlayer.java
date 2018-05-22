package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;

public class MultiPlayer extends Player {


    /**
     * Set to null the player parameters:
     * - coins are the player expendable coins for using the toolCard
     * - schemaCard
     * - privateObjectiveCard
     *
     * @param token                 string for locating the single player during the game
     * @param coin
     * @param schemaCard
     * @param privateObjectiveCards
     */
    public MultiPlayer(String token, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(token, coin, schemaCard, privateObjectiveCards);
    }

    /**
     * Return the score of the player based on the PrivateObjectiveCard, the remaining favor tokens and the empty spaces
     * of the schemaCard
     *
     * @return the score of the player for multiPlayerGame
     */
    @Override
    public int getVictoryPoints() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) + getCoins() - schemaCard.getNumberOfEmptySpaces();
    }
}
