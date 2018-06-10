package org.poianitibaldizhou.sagrada.game.model;

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

public interface IGame {

    /**
     * Attach a state observer to the game
     *
     * @param token token of the observer
     * @param stateObserver state fake observer that listens to modification of the game state
     */
    void attachStateObserver(String token, IStateFakeObserver stateObserver);

    /**
     * Attach a game observer to the game
     *
     * @param userToken token of the observer
     * @param gameObserver game fake observer that listens to modification of the game
     */
    void attachGameObserver(String userToken, IGameFakeObserver gameObserver);

    /**
     * Attach a round track observer to the round track of this game
     *
     * @param token token of the observer
     * @param roundTrackObserver round track fake observer that listens to modification of the round track of this game
     */
    void attachRoundTrackObserver(String token, IRoundTrackFakeObserver roundTrackObserver);

    /**
     * Attach a draft pool observer to the draft pool of this game
     *
     * @param token token of the observer
     * @param draftPoolObserver draft pool fake observer that listens to modification of the draft pool of this game
     */
    void attachDraftPoolObserver(String token, IDraftPoolFakeObserver draftPoolObserver);

    /**
     * Attach a tool card observer to a tool card of the game
     *
     * @param token token of the observer
     * @param toolCard tool card on which the observer listen
     * @param toolCardObserver tool card fake observer that listens to modification of toolCard
     */
    void attachToolCardObserver(String token, ToolCard toolCard, IToolCardFakeObserver toolCardObserver);

    /**
     * Attach a dice bag observer to the dice bag of this game
     *
     * @param token token of the observer
     * @param drawableCollectionObserver dice bag fake observer that listens to modification of the dice bag
     */
    void attachDiceBagObserver(String token, IDrawableCollectionFakeObserver<Dice> drawableCollectionObserver);

    /**
     * Attach a schema card observer to a specified schema card of this game
     *
     * @param token token of the observer
     * @param schemaCard schema card that will be listened by schemaCardObserver
     * @param schemaCardObserver schema card fake observer that listens to modification of schemaCard
     */
    void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardFakeObserver schemaCardObserver);

    /**
     * Attach a player observer to a specified player
     *
     * @param token token of the observer
     * @param player player that will be listened by playerObserver
     * @param playerObserver player fake observer that listens to modification of player
     */
    void attachPlayerObserver(String token, Player player, IPlayerFakeObserver playerObserver);

    /**
     * Detach all the fake observer related to token
     *
     * @param token token of the observer
     */
    void detachObservers(String token);

    void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException;

    void userJoin(String token) throws InvalidActionException;

    void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException;

    void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException;

    void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorFakeObserver executorObserver) throws InvalidActionException;

    void userChooseAction(String token, IActionCommand action) throws InvalidActionException;

    void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException;

    boolean containsToken(String token);

    String getName();

    List<Player> getPlayers();

    List<ToolCard> getToolCards();

    DraftPool getDraftPool();

    RoundTrack getRoundTrack();

    Player getCurrentPlayer() throws InvalidActionException;

    void forceStateChange() throws InvalidActionException;

    List<User> getTimedOutUsers();

    boolean isSinglePlayer();

    void forceGameTermination(Player winner);

    List<User> getUsers();

    List<PublicObjectiveCard> getPublicObjectiveCards();

    List<PrivateObjectiveCard> getPrivateObjectiveCardsByToken(String token);

    void forceSkipTurn() throws InvalidActionException;
}
