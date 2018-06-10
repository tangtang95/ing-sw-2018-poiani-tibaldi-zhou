package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;

/**
 * OVERVIEW: Interface for the class game. When a class has this interface as a reference for the game, only
 * this actions are allowed, thus limiting the coupling and the possible errors in modifying various stuff.
 */
public interface IGame {

    /**
     * Attach a state observer to the game
     *
     * @param token         token of the observer
     * @param stateObserver state fake observer that listens to modification of the game state
     */
    void attachStateObserver(String token, IStateFakeObserver stateObserver);

    /**
     * Attach a game observer to the game
     *
     * @param userToken    token of the observer
     * @param gameObserver game fake observer that listens to modification of the game
     */
    void attachGameObserver(String userToken, IGameFakeObserver gameObserver);

    /**
     * Attach a round track observer to the round track of this game
     *
     * @param token              token of the observer
     * @param roundTrackObserver round track fake observer that listens to modification of the round track of this game
     */
    void attachRoundTrackObserver(String token, IRoundTrackFakeObserver roundTrackObserver);

    /**
     * Attach a draft pool observer to the draft pool of this game
     *
     * @param token             token of the observer
     * @param draftPoolObserver draft pool fake observer that listens to modification of the draft pool of this game
     */
    void attachDraftPoolObserver(String token, IDraftPoolFakeObserver draftPoolObserver);

    /**
     * Attach a tool card observer to a tool card of the game
     *
     * @param token            token of the observer
     * @param toolCard         tool card on which the observer listen
     * @param toolCardObserver tool card fake observer that listens to modification of toolCard
     */
    void attachToolCardObserver(String token, ToolCard toolCard, IToolCardFakeObserver toolCardObserver);

    /**
     * Attach a dice bag observer to the dice bag of this game
     *
     * @param token                      token of the observer
     * @param drawableCollectionObserver dice bag fake observer that listens to modification of the dice bag
     */
    void attachDiceBagObserver(String token, IDrawableCollectionFakeObserver<Dice> drawableCollectionObserver);

    /**
     * Attach a schema card observer to a specified schema card of this game
     *
     * @param token              token of the observer
     * @param schemaCard         schema card that will be listened by schemaCardObserver
     * @param schemaCardObserver schema card fake observer that listens to modification of schemaCard
     */
    void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardFakeObserver schemaCardObserver);

    /**
     * Attach a player observer to a specified player
     *
     * @param token          token of the observer
     * @param player         player that will be listened by playerObserver
     * @param playerObserver player fake observer that listens to modification of player
     */
    void attachPlayerObserver(String token, Player player, IPlayerFakeObserver playerObserver);

    /**
     * Detach all the fake observer related to token
     *
     * @param token token of the observer
     */
    void detachObservers(String token);

    /**
     * Signals that an user (specified by token) has performed an event (such as notifying a dice, a color...)
     *
     * @param token player's token
     * @param event event that the user has fired
     * @throws InvalidActionException if the state instance != TurnState
     */
    void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException;

    /**
     * Signals to the game the a user has joined the game.
     * When all the player has joined, the setups begin.
     *
     * @param token token of the player who has joined the game
     * @throws InvalidActionException if the token is non-existent in the game ||
     *                                the player has already readied for the game
     */
    void userJoin(String token) throws InvalidActionException;

    /**
     * Signals that a player has chosen his schema card
     *
     * @param token player's token
     * @param schemaCard schema card chosen by the player
     * @throws InvalidActionException if the state instance != SetupPlayerState || the set schemaCard is unsuccessful
     */
    void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException;

    /**
     * Signals that a player has placed a dice
     *
     * @param token player's token
     * @param dice dice placed
     * @param position position on which the dice is placed on the player's schema card
     * @throws InvalidActionException if the state instance != TurnState ||
     *                                the placement of the dice is unsuccessful and inside the InvalidActionException
     *                                there is a RuleViolationException
     */
    void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException;

    /**
     * Signals that a player has used a certain tool card.
     *
     * @param token player's token
     * @param toolCard tool card used by the player
     * @param executorObserver executor fake observer for the tool card execution
     * @throws InvalidActionException if the state instance != TurnState ||
     *                                the player given is not the player turn ||
     *                                there are no coins to spend || if the tool card is not present in the game
     */
    void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorFakeObserver executorObserver) throws InvalidActionException;

    /**
     * Signals that the user has chosen an action to perform
     *
     * @param token player's token
     * @param action action that the player has chosen
     * @throws InvalidActionException if the state instance != TurnState || it's not the "player" turn
     */
    void userChooseAction(String token, IActionCommand action) throws InvalidActionException;

    /**
     * Signals the the player has chosen a certain private objective card.
     *
     * @param token player's token
     * @param privateObjectiveCard private objective card chosen by the player
     * @throws InvalidActionException if the state instance != EndGameState ||
     *                                the set of privateObjectiveCard is unsuccessful
     */
    void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException;

    /**
     * Returns true if token is contained in the list of tokens of the players of this game
     *
     * @param token player's token
     * @return true if the player is in the game, false otherwise
     */
    boolean containsToken(String token);

    /**
     * Returns the name of the game
     *
     * @return game's name
     */
    String getName();

    /**
     * Get the list of players of the game. Note that this list is available only after the phase of player setup.
     * The players are deep copied
     *
     * @return list of players
     */
    @Contract(pure = true)
    List<Player> getPlayers();

    /**
     * Get the list of tool cards which are present in the game.
     * The tool cards are deep copied
     *
     * @return list of tool cards
     */
    @Contract(pure = true)
    List<ToolCard> getToolCards();

    /**
     * Get the draft pool of the game. The draft pool is deep copied
     *
     * @return draft pool of the game
     */
    @Contract(pure = true)
    DraftPool getDraftPool();

    /**
     * Get the round track of the game.
     * The round track is deep copied.
     *
     * @return round track of the game
     */
    @Contract(pure = true)
    RoundTrack getRoundTrack();

    /**
     * Get the player who is currently playing.
     *
     * @return player who is playing
     * @throws InvalidActionException if no player is playing (e.g. in the setup phase of the game)
     */
    Player getCurrentPlayer() throws InvalidActionException;

    /**
     * Force the state to change to the next state of the finite state machine model of the game
     *
     * @throws InvalidActionException if state instance != TurnState or SetupPlayerState
     */
    void forceStateChange() throws InvalidActionException;

    /**
     * Return the list of users that have timed out during the choice of the schema cards at the start of the game
     *
     * @return list of users that have timed out
     */
    List<User> getTimedOutUsers();

    /**
     * Returns true if the game is single player, false otherwise
     *
     * @return true if the game is single player, false otherwise
     */
    boolean isSinglePlayer();

    /**
     * Force the termination of the game signaling a specified player as winner
     *
     * @param winner winner of the game
     */
    void forceGameTermination(Player winner);

    /**
     * Get the list of users that originally joined the game.
     * Deep copy.
     *
     * @return list of user of the game
     */
    List<User> getUsers();

    /**
     * Get the list of the public objective cards of the game
     *
     * @return public objective cards
     */
    List<PublicObjectiveCard> getPublicObjectiveCards();

    /**
     * Get the list of the private objective cards of a certain player
     *
     * @param token player's token
     * @return player's private objective cards
     */
    List<PrivateObjectiveCard> getPrivateObjectiveCardsByToken(String token);

    /**
     * Force the state to skip the turn of the current player
     *
     * @throws InvalidActionException if state instance != TurnState
     */
    void forceSkipTurn() throws InvalidActionException;
}
