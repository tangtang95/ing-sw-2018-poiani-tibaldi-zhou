package org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces;


import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;

/**
 * OVERVIEW: Fake observer for the game.
 * Fake observers are observer present on the server that listen to changes and modifications.
 * In this way, the network part is decoupled from the model.
 */
public interface IGameFakeObserver  {

    // NOTIFICATION TO ALL THE PLAYERS

    /**
     * Notify the list of players to the user
     *
     * @param users the list of players of the game
     */
    void onPlayersCreate(List<User> users) ;

    /**
     * Notify the list of public objective cards drawn in the game to the user
     *
     * @param publicObjectiveCards the list of public objective cards drawn
     */
    void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) ;

    /**
     * Notify the list of tool cards drawn in the game to the user
     *
     * @param toolCards the list of tool cards drawn
     */
    void onToolCardsDraw(List<ToolCard> toolCards) ;

    /**
     * Notify the player to choose one of the privateObjectiveCards
     *
     * @param privateObjectiveCards the list of privateObjectiveCards
     */
    void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) ;


    // NOTIFICATION TO ONLY ONE PLAYER

    /**
     * Notify the list of private objective cards drawn by the player (send it only to the user)
     *
     * @param privateObjectiveCards the list of private objective cards drawn
     *                              (1 if multi player game and 2 if single player game)
     */
    void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) ;

    /**
     * Notify the list of schema cards drawn by the player (send it only to the user)
     *
     * @param schemaCards the list of schema cards drawn
     */
    void onSchemaCardsDraw(List<FrontBackSchemaCard> schemaCards) ;
}