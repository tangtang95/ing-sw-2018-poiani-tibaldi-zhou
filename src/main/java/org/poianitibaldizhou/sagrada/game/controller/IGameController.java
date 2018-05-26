package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.*;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.network.IPingController;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameController extends IPingController, Remote {
    void joinGame(String token, String gameName, IGameView view, IGameObserver gameObserver,
                  IRoundTrackObserver roundTrackObserver, IStateObserver stateObserver,
                  IDraftPoolObserver draftPoolObserver, IDrawableCollectionObserver<Dice> diceBagObserver) throws RemoteException;

    void chooseSchemaCard(String token, String gameName, SchemaCard schemaCard) throws RemoteException;

    void bindPlayer(String token, String gameName, Player player, IPlayerObserver playerObserver
            , ISchemaCardObserver schemaCardObserver) throws RemoteException;

    void bindToolCard(String token, String gameName, ToolCard toolCard, IToolCardObserver toolCardObserver) throws RemoteException;

    void chooseAction(String token, String gameName, IActionCommand actionCommand) throws RemoteException;

    void placeDice(String token, String gameName, Dice dice, Position position) throws RemoteException;

    void useToolCard(String token, String gameName, ToolCard toolCard, IToolCardExecutorObserver executorObserver) throws RemoteException;

    void choosePrivateObjectiveCard(String token, String gameName, PrivateObjectiveCard privateObjectiveCard) throws RemoteException;

    /**
     * Set a dice on a certain toolcard for its purpose.
     * This method assumes that game's toolcards contains the specified toolcard.
     *
     * @param token        the user's token
     * @param gameName     game's name
     * @param dice         dice to set
     * @param toolCardName name of the ToolCard on which to place the dice
     */
    void setDice(String token, String gameName, Dice dice, String toolCardName) throws RemoteException;

    void setNewValue(String token, String gameName, int value, String toolCardName) throws RemoteException;

    void setColor(String token, String gameName, Color color, String toolCardName) throws RemoteException;

    void setPosition(String token, String gameName, Position position, String toolCardName) throws RemoteException;

}
