package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.io.IOException;
import java.rmi.Remote;
import java.util.Map;

public interface IGameController extends Remote {
    /**
     * Notifies that a player has joined the game.
     *
     * @param json               player's json that contains his token and the gameName
     * @param view               player's view
     * @param gameObserver       player's game observer
     * @param roundTrackObserver player's round track observer
     * @param stateObserver      player's state observer
     * @param draftPoolObserver  player's draft pool observer
     * @param diceBagObserver    player's dice bag observer
     * @throws IOException network communication error
     */
    void joinGame(String json, IGameView view, IGameObserver gameObserver,
                  IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                  IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver) throws IOException;

    /**
     * Notifies the schema card chosen by a certain player.
     * Player must be part of the specified game.
     *
     * @param json json containing player's token, his game name and the schema card that he had chosen
     * @throws IOException network communication error
     */
    void chosenSchemaCard(String json) throws IOException;

    /**
     * Binds the player  and che schema card observers of a certain player to the specified player and its schema card.
     * The player identified by token must be part of the specified game.
     *
     * @param json               json containing player's token, player that needs to be bind and
     * @param playerObserver     player's observer
     * @param schemaCardObserver schema card observer of the schema card of player
     * @throws IOException network communication error
     */
    void bindPlayer(String json, IPlayerObserver playerObserver
            , ISchemaCardObserver schemaCardObserver) throws IOException;

    /**
     * Binds the tool card observer of a certain player to the specified tool card.
     * Player must be part of the specified game.
     *
     * @param json             json containing player's token, game's name and toolcard that need to be binded
     * @param toolCardObserver player's tool card observer
     * @throws IOException network communication error
     */
    void bindToolCard(String json, IToolCardObserver toolCardObserver) throws IOException;

    /**
     * Notifies the action chosen by a certain player.
     * The player must be part of the specified game.
     *
     * @param json json containing player's token, game's name and action that the player has chosen
     * @throws IOException network communication error
     */
    void chooseAction(String json) throws IOException;

    /**
     * Notifies that a player wants to place a dice in a certain position.
     * The player must be part of the specified game.
     *
     * @param json json containing player's token, game's name, dice placed and position
     * @throws IOException network communication error
     */
    void placeDice(String json) throws IOException;

    /**
     * Notifies that a player wants to use a specific tool card.
     * The players must be part of the specified game.
     *
     * @param executorObserver player's observer for tool card execution
     * @param json             json containing player's token, game's name and tool card that the player wants to use
     * @throws IOException network communication error
     */
    void useToolCard(String json, IToolCardExecutorObserver executorObserver) throws IOException;

    /**
     * Indicates the chosen private objective cards of a certain player.
     * The player must be part of the specified game.
     *
     * @param json json containing player's token, game's name and private objective card chosen by the
     *             player
     * @throws IOException network communication error
     */
    void choosePrivateObjectiveCard(String json) throws IOException;

    /**
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param json json containing player's token, game's name and dice to set
     */
    void setDice(String json) throws IOException;

    /**
     * Set a a new value needed for a dice on a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param json json containing game's name player's token and value to set
     */
    void setNewValue(String json) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param json json containing game played, player's token and color to set
     */
    void setColor(String json) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param json json containing game's name, player's token and position to set
     * @throws IOException
     */
    void setPosition(String json) throws IOException;

    /**
     * Re-connects a player to a certain game.
     * The player must be checked as disconnected and must be part of the specified game
     *
     * @param json               json containing player's token and game's name
     * @param gameView           player's game view
     * @param stateObserver      player's state observer
     * @param playerObserver     player's players observer (the key of the map are the tokens of the players in the game)
     * @param toolCardObserver   player's tool card observer (the key are the tool card's name)
     * @param schemaCardObserver player's schema card observers (the key are the player's token owning that card)
     * @param gameObserver       player's game observer
     * @param draftPoolObserver  player's draft pool observer
     * @param roundTrackObserver player's round track observer
     * @param diceBagObserver    player's dice bag observer
     * @throws IOException network communication error
     */
    void reconnect(String json, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                   Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                   IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver
                           diceBagObserver) throws IOException;

    /**
     * Get all the tool cards of a certain game
     *
     * @param json json containing the token of the player requesting the tool cards and the game's name
     * @throws IOException network communication error
     */
    String getToolCards(String json) throws IOException;

    /**
     * Get the draft pool of a certain game
     *
     * @param json json containing the token of the player requesting the draft pool and the game's name
     * @throws IOException network communication error
     */
    String getDraftPool(String json) throws IOException;

    /**
     * Get the round track of a certain game
     *
     * @param json json containing the token of the player requesting the draft pool and the name of the game
     * @throws IOException network communication error
     */
    String getRoundTrack(String json) throws IOException;

    /**
     * Get the tool card of a certain game
     *
     * @param json json containing player's token, game's name and name of the requested tool card
     * @throws IOException network communication error
     */
    String getToolCardByName(String json) throws IOException;

    /**
     * Get the current player of a certain game
     *
     * @param json json containing player's token and game name
     * @throws IOException network communication error
     */
    String getCurrentPlayer(String json) throws IOException;
}
