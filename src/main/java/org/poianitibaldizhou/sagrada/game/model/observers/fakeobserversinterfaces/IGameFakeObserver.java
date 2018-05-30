package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;


import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.io.IOException;
import java.rmi.Remote;
import java.util.List;

public interface IGameFakeObserver  {

    // NOTIFICATION TO ALL THE PLAYERS

    /**
     * Notify the list of players to the user
     *
     * @param players the list of players of the game
     * @throws IOException network error
     */
    void onPlayersCreate(List<Player> players) throws IOException;

    /**
     * Notify the list of public objective cards drawn in the game to the user
     *
     * @param publicObjectiveCards the list of public objective cards drawn
     * @throws IOException network error
     */
    void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) throws IOException;

    /**
     * Notify the list of tool cards drawn in the game to the user
     *
     * @param toolCards the list of tool cards drawn
     * @throws IOException network error
     */
    void onToolCardsDraw(List<ToolCard> toolCards) throws IOException;

    /**
     * Notify the player to choose one of the privateObjectiveCards
     *
     * @param privateObjectiveCards the list of privateObjectiveCards
     * @throws IOException network error
     */
    void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) throws IOException;


    // NOTIFICATION TO ONLY ONE PLAYER

    /**
     * Notify the list of private objective cards drawn by the player (send it only to the user)
     *
     * @param privateObjectiveCards the list of private objective cards drawn
     *                              (1 if multi player game and 2 if single player game)
     * @throws IOException network error
     */
    void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) throws IOException;

    /**
     * Notify the list of schema cards drawn by the player (send it only to the user)
     *
     * @param schemaCards the list of schema cards drawn
     * @throws IOException network error
     */
    void onSchemaCardsDraw(List<FrontBackSchemaCard> schemaCards) throws IOException;

}