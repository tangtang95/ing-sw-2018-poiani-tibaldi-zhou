package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import java.io.IOException;
import java.rmi.Remote;

/**
 * OVERVIEW: Real observer for the game. Real observer are observers client side that
 * listen to changes and modification of the model. All the parameters of the methods are
 * protocol string.
 */
public interface IGameObserver extends Remote {

    /**
     * Notify the list of players to the user
     *
     * @param players the list of players of the game
     * @throws IOException network error
     */
    void onPlayersCreate(String players) throws IOException;

    /**
     * Notify the list of public objective cards drawn in the game to the user
     *
     * @param publicObjectiveCards the list of public objective cards drawn
     * @throws IOException network error
     */
    void onPublicObjectiveCardsDraw(String publicObjectiveCards) throws IOException;

    /**
     * Notify the list of tool cards drawn in the game to the user
     *
     * @param toolCards the list of tool cards drawn
     * @throws IOException network error
     */
    void onToolCardsDraw(String toolCards) throws IOException;

    /**
     * Notify the player to choose one of the privateObjectiveCards
     *
     * @param privateObjectiveCards the list of privateObjectiveCards
     * @throws IOException network error
     */
    void onChoosePrivateObjectiveCards(String privateObjectiveCards) throws IOException;


    // NOTIFICATION TO ONLY ONE PLAYER

    /**
     * Notify the list of private objective cards drawn by the player (send it only to the user)
     *
     * @param privateObjectiveCards the list of private objective cards drawn
     *                              (1 if multi player game and 2 if single player game)
     * @throws IOException network error
     */
    void onPrivateObjectiveCardDraw(String privateObjectiveCards) throws IOException;

    /**
     * Notify the list of schema cards drawn by the player (send it only to the user)
     *
     * @param schemaCards the list of schema cards drawn
     * @throws IOException network error
     */
    void onSchemaCardsDraw(String schemaCards) throws IOException;
}
