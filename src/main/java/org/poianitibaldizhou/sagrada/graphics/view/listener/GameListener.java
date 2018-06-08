package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.GameView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameListener extends UnicastRemoteObject implements IGameView, IGameObserver{

    private final transient GameView gameView;
    private final transient MultiPlayerController controller;

    public GameListener(GameView gameView) throws RemoteException {
        this.gameView = gameView;
        this.controller = gameView.getController();
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
    public void onPlayersCreate(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<UserWrapper> users = parser.getListOfUserWrapper(message);

        Platform.runLater(()->{
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap;
            List<PrivateObjectiveCardWrapper> privateObjectiveCardWrappers;
            try {
                privateObjectiveCardWrappers = controller.getOwnPrivateObjectiveCard();
                schemaCardWrapperMap = controller.getSchemaCardMap();
            } catch (IOException e) {
                gameView.showSevereErrorMessage("Errore di connessione");
                return;
            }

            gameView.clearNotifyPane();
            gameView.deactivateNotifyPane();
            gameView.drawUsers(users, schemaCardWrapperMap, privateObjectiveCardWrappers);
        });
    }

    @Override
    public void onPublicObjectiveCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PublicObjectiveCardWrapper> publicObjectiveCardWrappers = parser.getPublicObjectiveCards(message);

        Platform.runLater(()->{
            gameView.drawPublicObjectiveCards(publicObjectiveCardWrappers);
        });

    }

    @Override
    public void onToolCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<ToolCardWrapper> toolCardWrappers = parser.getToolCards(message);

        Platform.runLater(() -> {
            gameView.drawToolCards(toolCardWrappers);
        });
    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) throws IOException {

    }

    @Override
    public void onPrivateObjectiveCardDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<PrivateObjectiveCardWrapper> privateObjectiveCards = parser.getPrivateObjectiveCards(message);
        Platform.runLater(() -> {
            gameView.showPrivateObjectiveCards(privateObjectiveCards);
        });
    }

    @Override
    public void onSchemaCardsDraw(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        List<FrontBackSchemaCardWrapper> frontBackSchemaCards = parser.getFrontBackSchemaCards(message);

        Platform.runLater(() -> {
            gameView.activateNotifyPane();
            gameView.clearNotifyPane();
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
