package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

public interface IGame {

    void attachStateObserver(String token, IStateObserver stateObserver);

    void attachGameObserver(String userToken, IGameObserver gameObserver);

    void attachRoundTrackObserver(String token, IRoundTrackObserver roundTrackObserver);

    void attachDraftPoolObserver(String token, IDraftPoolObserver draftPoolObserver);

    void attachToolCardObserver(String token, ToolCard toolCard, IToolCardObserver toolCardObserver) throws InvalidActionException;

    void attachDiceBagObserver(String token, IDrawableCollectionObserver<Dice> drawableCollectionObserver);

    void attachSchemaCardObserver(String token, SchemaCard schemaCard, ISchemaCardObserver schemaCardObserver) throws InvalidActionException;

    void attachPlayerObserver(String token, Player player, IPlayerObserver playerObserver) throws InvalidActionException;

    void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException;

    void userJoin(String token) throws InvalidActionException;

    void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException;

    void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException;

    void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws InvalidActionException;

    void userChooseAction(String token, IActionCommand action) throws InvalidActionException;

    void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException;

    boolean containsToken(String token);

    String getName();
}
