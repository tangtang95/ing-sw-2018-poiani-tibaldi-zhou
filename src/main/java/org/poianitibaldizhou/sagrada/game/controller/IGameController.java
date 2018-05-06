package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;

import java.rmi.RemoteException;

public interface IGameController {
    void joinGame(Game game, IGameView view, String token) throws RemoteException;
    void setDice(Dice dice, Game game, ToolCard toolCard) throws RemoteException;
}
