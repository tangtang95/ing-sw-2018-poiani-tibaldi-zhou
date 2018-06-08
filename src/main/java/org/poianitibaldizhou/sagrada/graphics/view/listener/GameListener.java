package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.view.GameView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.FrontBackSchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PrivateObjectiveCardWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

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
    public void onPrivateObjectiveCardDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PrivateObjectiveCardWrapper> privateObjectiveCards = parser.getPrivateObjectiveCards(message);
        Platform.runLater(() -> {
            gameView.activateNotifyPane();
            gameView.clearNotifyPane();
            gameView.showPrivateObjectiveCards(privateObjectiveCards);
        });
    }

    @Override
    public void onSchemaCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<FrontBackSchemaCardWrapper> frontBackSchemaCards = parser.getFrontBackSchemaCards(message);
        Platform.runLater(() -> {
            gameView.showFrontBackSchemaCards(frontBackSchemaCards);
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GameListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}
