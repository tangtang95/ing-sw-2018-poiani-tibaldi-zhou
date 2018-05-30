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
     * @param token              player's token
     * @param gameName           game's name
     * @param view               player's view
     * @param gameObserver       player's game observer
     * @param roundTrackObserver player's round track observer
     * @param stateObserver      player's state observer
     * @param draftPoolObserver  player's draft pool observer
     * @param diceBagObserver    player's dice bag observer
     * @throws IOException network communication error
     */
    void joinGame(String token, String gameName, IGameView view, IGameObserver gameObserver,
                  IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                  IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver diceBagObserver) throws IOException;

    /**
     * Notifies the schema card chosen by a certain player.
     * Player must be part of the specified game.
     *
     * @param token      player's token
     * @param gameName   game's name
     * @param schemaCard schema card chosen by a certain player
     * @throws IOException network communication error
     */
    void chooseSchemaCard(String token, String gameName, SchemaCard schemaCard) throws IOException;

    /**
     * Binds the player  and che schema card observers of a certain player to the specified player and its schema card.
     * The player identified by token must be part of the specified game.
     *
     * @param token              player's token
     * @param gameName           game's name
     * @param player             player that is going to be observer by the specified observer
     * @param playerObserver     player's observer
     * @param schemaCardObserver schema card observer of the schema card of player
     * @throws IOException network communication error
     */
    void bindPlayer(String token, String gameName, Player player, IPlayerObserver playerObserver
            , ISchemaCardObserver schemaCardObserver) throws IOException;

    /**
     * Binds the tool card observer of a certain player to the specified tool card.
     * Player must be part of the specified game.
     *
     * @param token            player's token
     * @param gameName         game's name
     * @param toolCard         tool card regarding the specified observer
     * @param toolCardObserver player's tool card observer
     * @throws IOException network communication error
     */
    void bindToolCard(String token, String gameName, ToolCard toolCard, IToolCardObserver toolCardObserver) throws IOException;

    /**
     * Notifies the action chosen by a certain player.
     * The player must be part of the specified game.
     *
     * @param token         player's token
     * @param gameName      game's name
     * @param actionCommand action that the player has chosen
     * @throws IOException network communication error
     */
    void chooseAction(String token, String gameName, IActionCommand actionCommand) throws IOException;

    /**
     * Notifies that a player wants to place a dice in a certain position.
     * The player must be part of the specified game.
     *
     * @param token    player's token
     * @param gameName game's name
     * @param dice     dice that the player wants to place
     * @param position dice needs to be placed in this position
     * @throws IOException network communication error
     */
    void placeDice(String token, String gameName, Dice dice, Position position) throws IOException;

    /**
     * Notifies that a player wants to use a specific tool card.
     * The players must be part of the specified game.
     *
     * @param token            player's token
     * @param gameName         game's name
     * @param toolCard         tool card that the player wants to use
     * @param executorObserver player's observer for tool card execution
     * @throws IOException network communication error
     */
    void useToolCard(String token, String gameName, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws IOException;

    /**
     * Indicates the chosen private objective cards of a certain player.
     * The player must be part of the specified game.
     *
     * @param token                player's token
     * @param gameName             game's name
     * @param privateObjectiveCard private objective card chosen by the player
     * @throws IOException network communication error
     */
    void choosePrivateObjectiveCard(String token, String gameName, PrivateObjectiveCard privateObjectiveCard) throws IOException;

    /**
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token        the user's token
     * @param gameName     game's name
     * @param dice         dice to set
     * @param toolCardName name of the ToolCard on which to place the dice
     */
    void setDice(String token, String gameName, Dice dice, String toolCardName) throws IOException;

    /**
     * Set a a new value needed for a dice on a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param value        dice's value
     * @param toolCardName toolcard on which to place the value
     */
    void setNewValue(String token, String gameName, int value, String toolCardName) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param color        dice's value
     * @param toolCardName toolcard on which to place the color
     */
    void setColor(String token, String gameName, Color color, String toolCardName) throws IOException;

    /**
     * Set a new color needed for a certain toolcard and its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token
     * @param gameName     game played
     * @param position     position that needs to be set for the specified toolcard
     * @param toolCardName toolcard on which to set the posiiton
     * @throws IOException
     */
    void setPosition(String token, String gameName, Position position, String toolCardName) throws IOException;

    /**
     * Re-connects a player to a certain game.
     * The player must be checked as disconnected and must be part of the specified game
     *
     * @param token              player's token
     * @param gameName           game's name
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
    void reconnect(String token, String gameName, IGameView gameView, IStateObserver stateObserver, Map<String, IPlayerObserver> playerObserver,
                   Map<String, IToolCardObserver> toolCardObserver, Map<String, ISchemaCardObserver> schemaCardObserver, IGameObserver gameObserver,
                   IDraftPoolObserver draftPoolObserver, IRoundTrackObserver roundTrackObserver, IDrawableCollectionObserver
                           diceBagObserver) throws IOException;

    /**
     * Synchronize the model of a certain player. The player must be part of the specified game
     *
     * @param token    player's token
     * @param gameName game's name
     * @throws IOException network communication error
     */
    void synchronizeModel(String token, String gameName) throws IOException;

    /**
     * Get all the tool cards of a certain game
     *
     * @param token token of the player requesting the tool cards
     * @param gameName games'e name
     * @throws IOException network communication error
     */
    String getToolCards(String token, String gameName) throws IOException;

    /**
     * Get the draft pool of a certain game
     *
     * @param token token of the player requesting the draft pool
     * @param gameName game's name
     * @throws IOException network communication error
     */
    String getDraftPool(String token, String gameName) throws IOException;

    /**
     * Get the round track of a certain game
     *
     * @param token token of the player requesting the draft pool
     * @param gameName game's name
     * @throws IOException network communication error
     */
    String getRoundTrack(String token, String gameName) throws IOException;

    /**
     * Get the tool card of a certain game
     *
     * @param token player's token
     * @param gameName game's name
     * @param toolCardName name of the requested tool card
     * @throws IOException network communication error
     */
    String getToolCardByName(String token, String gameName, String toolCardName) throws IOException;

    /**
     * Get the current player of a certain game
     *
     * @param token player's token
     * @param gameName game's name
     * @throws IOException network communication error
     */
    String getCurrentPlayer(String token, String gameName) throws IOException;
}
