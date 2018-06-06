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
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.List;

public interface IGame {

    void attachStateObserver(String token, IStateFakeObserver stateObserver);

    void attachGameObserver(String userToken, IGameFakeObserver gameObserver);

    void attachRoundTrackObserver(String token, IRoundTrackFakeObserver roundTrackObserver);

    void attachDraftPoolObserver(String token, IDraftPoolFakeObserver draftPoolObserver);

    void attachToolCardObserver(String token, ToolCard toolCard, IToolCardFakeObserver toolCardObserver);

    void attachDiceBagObserver(String token, IDrawableCollectionFakeObserver<Dice> drawableCollectionObserver);

    void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardFakeObserver schemaCardObserver);

    void attachPlayerObserver(String token, Player player, IPlayerFakeObserver playerObserver);

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

    boolean isSinglePlayer();

    void forceGameTermination(Player winner);

    List<User> getUsers();

    List<PublicObjectiveCard> getPublicObjectiveCards();

    List<PrivateObjectiveCard> getPrivateObjectiveCardsByToken(String token);
}
