package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;

import java.rmi.RemoteException;

public interface IGame {

    void attachStateObserver(IStateObserver stateObserver);

    void attachGameObserver(String userToken, IGameObserver gameObserver);

    void attachRoundTrackObserver(IRoundTrackObserver roundTrackObserver);

    void attachDraftPoolObserver(IDraftPoolObserver draftPoolObserver);

    void attachToolCardObserver(ToolCard toolCard, IToolCardObserver toolCardObserver) throws InvalidActionException;

    void attachDiceBagObserver(IDrawableCollectionObserver<Dice> drawableCollectionObserver);

    void attachSchemaCardObserver(SchemaCard schemaCard, ISchemaCardObserver schemaCardObserver) throws InvalidActionException;

    void attachPlayerObserver(Player player, IPlayerObserver playerObserver) throws InvalidActionException;

    void userFireExecutorEvent(String token, ExecutorEvent event) throws InvalidActionException;

    void userJoin(String token) throws InvalidActionException, RemoteException;

    void userSelectSchemaCard(String token, SchemaCard schemaCard) throws InvalidActionException, RemoteException;

    void userPlaceDice(String token, Dice dice, Position position) throws InvalidActionException, RemoteException;

    void userUseToolCard(String token, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws InvalidActionException, RemoteException;

    void userChooseAction(String token, IActionCommand action) throws InvalidActionException, RemoteException;

    void userChoosePrivateObjectiveCard(String token, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException, RemoteException;

    boolean containsToken(String token);

    String getName();
}
