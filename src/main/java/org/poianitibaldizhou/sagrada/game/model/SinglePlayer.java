package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;

public class SinglePlayer extends Player {


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
    public SinglePlayer(String token, ICoin coin, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        super(token, coin, schemaCard, privateObjectiveCards);
    }

    /**
     *
     * @return the score of the player for singlePlayerGame
     */
    @Override
    public int getVictoryPoints() {
        return privateObjectiveCards.get(indexOfPrivateObjectiveCard)
                .getScore(schemaCard) - schemaCard.getNumberOfEmptySpaces()*3;
    }
}
