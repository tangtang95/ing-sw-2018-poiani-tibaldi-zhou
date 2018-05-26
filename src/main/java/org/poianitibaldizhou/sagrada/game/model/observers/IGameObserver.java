package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGameObserver extends Remote {

    // NOTIFICATION TO ALL THE PLAYERS

    /**
     * Notify the list of players to the user
     *
     * @param players the list of players of the game
     * @throws RemoteException network error
     */
    void onPlayersCreate(List<Player> players) throws RemoteException;

    /**
     * Notify the list of public objective cards drawn in the game to the user
     *
     * @param publicObjectiveCards the list of public objective cards drawn
     * @throws RemoteException network error
     */
    void onPublicObjectiveCardsDraw(List<PublicObjectiveCard> publicObjectiveCards) throws RemoteException;

    /**
     * Notify the list of tool cards drawn in the game to the user
     *
     * @param toolCards the list of tool cards drawn
     * @throws RemoteException network error
     */
    void onToolCardsDraw(List<ToolCard> toolCards) throws RemoteException;

    /**
     * Notify the player to choose one of the privateObjectiveCards
     *
     * @param privateObjectiveCards the list of privateObjectiveCards
     * @throws RemoteException network error
     */
    void onChoosePrivateObjectiveCards(List<PrivateObjectiveCard> privateObjectiveCards) throws RemoteException;


    // NOTIFICATION TO ONLY ONE PLAYER

    /**
     * Notify the list of private objective cards drawn by the player (send it only to the user)
     *
     * @param privateObjectiveCards the list of private objective cards drawn
     *                              (1 if multi player game and 2 if single player game)
     * @throws RemoteException network error
     */
    void onPrivateObjectiveCardDraw(List<PrivateObjectiveCard> privateObjectiveCards) throws RemoteException;

    /**
     * Notify the list of schema cards drawn by the player (send it only to the user)
     *
     * @param schemaCards the list of schema cards drawn
     * @throws RemoteException network error
     */
    void onSchemaCardsDraw(List<List<SchemaCard>> schemaCards) throws RemoteException;

}
