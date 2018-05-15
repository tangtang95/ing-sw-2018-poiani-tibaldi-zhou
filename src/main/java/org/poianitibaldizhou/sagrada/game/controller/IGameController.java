package org.poianitibaldizhou.sagrada.game.controller;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.view.IGameView;

import java.rmi.RemoteException;

public interface IGameController {
    void joinGame(IGameView view, String token, String gameName) throws RemoteException;
    void setDice(Dice dice, String gameName, String toolCardName) throws RemoteException;
    void setNewValue(int value, String gameName, String toolCardName) throws RemoteException;
    void setColor(Color color, String gameName, String toolCardName) throws RemoteException;
    void setPosition(Position position, String gameName, String toolCardName) throws RemoteException;
}
