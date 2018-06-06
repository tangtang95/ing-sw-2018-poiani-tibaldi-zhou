package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.view.GameView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameListener extends UnicastRemoteObject implements IGameView, IGameObserver{

    private final transient GameView gameView;

    public GameListener(GameView gameView) throws RemoteException {
        this.gameView = gameView;
    }

    public GameView getGameView() {
        return gameView;
    }

    @Override
    public void ack(String ack) throws IOException {

    }

    @Override
    public void err(String err) throws IOException {

    }

    @Override
    public void onPlayersCreate(String players) throws IOException {

    }

    @Override
    public void onPublicObjectiveCardsDraw(String publicObjectiveCards) throws IOException {

    }

    @Override
    public void onToolCardsDraw(String toolCards) throws IOException {

    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) throws IOException {

    }

    @Override
    public void onPrivateObjectiveCardDraw(String privateObjectiveCards) throws IOException {

    }

    @Override
    public void onSchemaCardsDraw(String schemaCards) throws IOException {

    }
}
