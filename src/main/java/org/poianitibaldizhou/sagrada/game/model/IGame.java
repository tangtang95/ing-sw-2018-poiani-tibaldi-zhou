package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import java.rmi.RemoteException;
import java.util.List;

public interface IGame {

    void attachStateObserver(String token, StateFakeObserver stateObserver);

    void attachGameObserver(String userToken, GameFakeObserver gameObserver);

    void attachRoundTrackObserver(String token, RoundTrackFakeObserver roundTrackObserver);

    void attachDraftPoolObserver(String token, DraftPoolFakeObserver draftPoolObserver);

    void attachToolCardObserver(String token, ToolCard toolCard, ToolCardFakeObserver toolCardObserver);

    void attachDiceBagObserver(String token, DrawableCollectionFakeObserver<Dice> drawableCollectionObserver);

    void attachSchemaCardObserver(String token, SchemaCard schemaCard, SchemaCardFakeObserver schemaCardObserver);

    void attachPlayerObserver(String token, Player player, PlayerFakeObserver playerObserver);

    void detachObservers(String token);

    void userFireExecutorEvent(String token, ExecutorEvent event) throws IllegalArgumentException, InvalidActionException;

    void userJoin(String token) throws IllegalArgumentException, InvalidActionException;

    void userSelectSchemaCard(String token, SchemaCard schemaCard) throws IllegalArgumentException, InvalidActionException;

    void userPlaceDice(String token, Dice dice, Position position) throws IllegalArgumentException, InvalidActionException;

    void userUseToolCard(String token, ToolCard toolCard, ToolCardExecutorFakeObserver executorObserver) throws IllegalArgumentException, InvalidActionException;

    void userChooseAction(String token, IActionCommand action) throws IllegalArgumentException, InvalidActionException;

    void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws IllegalArgumentException, InvalidActionException;

    boolean containsToken(String token);

    String getName();

    List<Player> getPlayers();

    List<ToolCard> getToolCards();

    DraftPool getDraftPool();

    RoundTrack getRoundTrack();

    Player getCurrentPlayer() throws InvalidActionException;
}
