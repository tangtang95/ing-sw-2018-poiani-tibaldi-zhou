package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.List;

public interface IGameObserver {

    // NOTIFICATION TO ALL THE PLAYERS
    /**
     * Notify the list of players to the user
     * @param players the list of players of the game
     */
    void onPlayersCreate(List<Player> players);

    /**
     * Notify the list of public objective cards drawn in the game to the user
     * @param publicObjectiveCards the list of public objective cards drawn
     */
    void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards);

    /**
     * Notify the list of tool cards drawn in the game to the user
     * @param toolCards the list of tool cards drawn
     */
    void onToolCardsDraw(List<ToolCard> toolCards);


    // NOTIFICATION TO ONLY ONE PLAYER
    /**
     * Notify the list of private objective cards drawn by the player (send it only to the user)
     *
     * @param privateObjectiveCards the list of private objective cards drawn
     *                              (1 if multi player game and 2 if single player game)
     */
    void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards);

    /**
     * Notify the list of schema cards drawn by the player (send it only to the user)
     * @param schemaCards the list of schema cards drawn
     */
    void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards);

}
