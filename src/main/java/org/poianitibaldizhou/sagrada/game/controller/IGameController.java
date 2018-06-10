package org.poianitibaldizhou.sagrada.game.controller;


import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.view.IGameView;


import java.io.IOException;
import java.rmi.Remote;
import java.util.Map;

/**
 * OVERVIEW: Game controller: it handles that client requests and gives input to the model. The model will update, manipulates
 * and retrieves its data in order to follow the client inputs.
 */
public interface IGameController extends Remote {
    /**
     * Notifies that a player has joined the game.
     *
     * @param message            player's message that contains his token and the gameName
     * @param view               player's view
     * @param gameObserver       player's game observer
     * @param roundTrackObserver player's round track observer
     * @param stateObserver      player's state observer
     * @param draftPoolObserver  player's draft pool observer
     * @param diceBagObserver    player's dice bag observer
     * @param timeOutObserver    player's time out observer
     * @throws IOException network communication error
     */
    void joinGame(String message, IGameView view, IGameObserver gameObserver,
                  IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                  IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver, ITimeOutObserver timeOutObserver) throws IOException;

    /**
     * Notifies the schema card chosen by a certain player.
     * Player must be part of the specified game.
     *
     * @param message message containing player's token, his game name and the schema card that he had chosen
     * @throws IOException network communication error
     */
    void chooseSchemaCard(String message) throws IOException;

    /**
     * Binds the player  and the schema card observers of a certain player to the specified player and its schema card.
     * The player identified by token must be part of the specified game.
     *
     * @param message            message containing player's token, username of the player on which the bind acts and
     *                           the game name
     * @param playerObserver     player's observer
     * @param schemaCardObserver schema card observer of the schema card of player
     * @throws IOException network communication error
     */
    void bindPlayer(String message, IPlayerObserver playerObserver
            , ISchemaCardObserver schemaCardObserver) throws IOException;

    /**
     * Binds the tool card observer of a certain player to the specified tool card.
     * Player must be part of the specified game.
     *
     * @param message          message containing player's token, game's name and toolCard that need to be binded
     * @param toolCardObserver player's tool card observer
     * @throws IOException network communication error
     */
    void bindToolCard(String message, IToolCardObserver toolCardObserver) throws IOException;

    /**
     * Notifies the action chosen by a certain player.
     * The player must be part of the specified game.
     *
     * @param message message containing player's token, game's name and action that the player has chosen
     * @throws IOException network communication error
     */
    void chooseAction(String message) throws IOException;

    /**
     * Notifies that a player wants to place a dice in a certain position.
     * The player must be part of the specified game.
     *
     * @param message message containing player's token, game's name, dice placed and position
     * @throws IOException network communication error
     */
    void placeDice(String message) throws IOException;

    /**
     * Notifies that a player wants to use a specific tool card.
     * The players must be part of the specified game.
     *
     * @param executorObserver player's observer for tool card execution
     * @param message          message containing player's token, game's name and tool card that the player wants to use
     * @throws IOException network communication error
     */
    void useToolCard(String message, IToolCardExecutorObserver executorObserver) throws IOException;

    /**
     * Indicates the chosen private objective cards of a certain player.
     * The player must be part of the specified game.
     *
     * @param message message containing player's token, game's name and private objective card chosen by the
     *                player
     * @throws IOException network communication error
     */
    void choosePrivateObjectiveCard(String message) throws IOException;

    /**
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param message message containing player's token, game's name and dice to set
     * @throws IOException network communication error
     */
    void setDice(String message) throws IOException;

    /**
     * Set a a new value needed for a dice on a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param message message containing game's name player's token and value to set
     */
    void setNewValue(String message) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param message message containing game played, player's token and color to set
     * @throws IOException network communication error
     */
    void setColor(String message) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param message message containing game's name, player's token and position to set
     * @throws IOException network communication error
     */
    void setPosition(String message) throws IOException;

    /**
     * Set the player action to continue or not the action.
     *
     * @param message message containing game's name, player's token and player choice
     * @throws IOException network communication error
     */
    void setContinueAction(String message) throws IOException;

    /**
     * Re-connects a player to a certain game.
     * The player must be checked as disconnected and must be part of the specified game
     *
     * @param message            message containing player's username
     * @param gameView           player's game view
     * @param stateObserver      player's state observer
     * @param playerObserver     player's players observer (the key of the map are the tokens of the players in the game)
     * @param toolCardObserver   player's tool card observer (the key are the tool card's name)
     * @param schemaCardObserver player's schema card observers (the key are the player's token owning that card)
     * @param gameObserver       player's game observer
     * @param draftPoolObserver  player's draft pool observer
     * @param roundTrackObserver player's round track observer
     * @param diceBagObserver    player's dice bag observer
     * @param timeOutObserver    player's time out observer
     * @return player's token
     * @throws IOException network communication error
     */
    void reconnect(String message, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                   Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                   IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver
                           diceBagObserver, ITimeOutObserver timeOutObserver) throws IOException;

    /**
     * Check if a player can reconnect
     *
     * @param message message containing player's username
     * @return an error message if the reconnection is not possible for the specified username; otherwise the player list,
     * the game name and the player token
     * @throws IOException network communication error
     */
    String attemptReconnect(String message) throws IOException;

    /**
     * Get all the tool cards of a certain game
     *
     * @param message message containing the token of the player requesting the tool cards and the game's name
     * @throws IOException network communication error
     */
    String getToolCards(String message) throws IOException;

    /**
     * Get the coins of a certain game and a certain user
     *
     * @param message message containing the token of the player requesting the coins and the game's name
     * @throws IOException network communication error
     */
    String getMyCoins(String message) throws IOException;

    /**
     * Get all the coins of a certain game
     *
     * @param message message containing the token of the player requesting the coins and the game's name
     * @throws IOException network communication error
     */
    String getPlayersCoins(String message) throws IOException;

    /**
     * Get all the public objective cards of a certain game
     *
     * @param message message containing the token of the player requesting the public cards and the game's name
     * @return protocol message containing the public cards
     * @throws IOException network communication error
     */
    String getPublicObjectiveCards(String message) throws IOException;

    /**
     * Get all the private objective cards of a certain player
     *
     * @param message message containing the token of the player requesting his private cards and the game's name
     * @return protocol message containing the private cards
     * @throws IOException network communication error
     */
    String getPrivateObjectiveCardByToken(String message) throws IOException;

    /**
     * Get all the schema cards of a certain game
     *
     * @param message message containing the token of the player requesting the schema cards and the game's name
     * @return protocol message containing the schema cards associated with that player
     * @throws IOException network communication error
     */
    String getSchemaCards(String message) throws IOException;

    /**
     * Get the draft pool of a certain game
     *
     * @param message message containing the token of the player requesting the draft pool and the game's name
     * @throws IOException network communication error
     */
    String getDraftPool(String message) throws IOException;

    /**
     * Get the round track of a certain game
     *
     * @param message message containing the token of the player requesting the draft pool and the name of the game
     * @throws IOException network communication error
     */
    String getRoundTrack(String message) throws IOException;

    /**
     * Get the tool card of a certain game
     *
     * @param message message containing player's token, game's name and name of the requested tool card
     * @throws IOException network communication error
     */
    String getToolCardByName(String message) throws IOException;

    /**
     * Get the current player of a certain game
     *
     * @param message message containing player's token and game name
     * @throws IOException network communication error
     */
    String getCurrentPlayer(String message) throws IOException;


    /**
     * Get the schema card of a certain player
     *
     * @param message message containing player's token and game name
     * @return a message containing the schema card
     * @throws IOException network communication error
     */
    String getSchemaCardByToken(String message) throws IOException;

    /**
     * Get the list of user (username) of a certain game
     *
     * @param message a protocol message containing player's token and game name
     * @return a protocol message containing the list of user of the game by its name
     * @throws IOException network communication error
     */
    String getListOfUser(String message) throws IOException;


    /**
     * Get timeout for the current player move or for the time that
     *
     * @param message message containing the token of the player requesting the action
     *                and the name of the game
     * @return protocol message containing the time to timeout
     * @throws IOException network communication error
     */
    String getTimeout(String message) throws IOException;

    /**
     * Creates a single player game.
     *
     * @param message protocol message containing  player's username and game difficulty
     * @return message containing game's name and player's token or errors
     */
    String createSinglePlayer(String message) throws IOException;

    /**
     * Signals that a player has chosen to quit the game
     *
     * @param message protocol message containing player's token and the name of the game
     * @throws IOException network communication error
     */
    void quitGame(String message) throws IOException;
}
