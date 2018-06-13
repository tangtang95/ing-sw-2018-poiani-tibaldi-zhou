package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.poianitibaldizhou.sagrada.exception.NetworkException;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.utils.Difficulty;
import org.poianitibaldizhou.sagrada.graphics.view.IGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.MultiPlayerGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.SinglePlayerGameViewStrategy;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;
import org.poianitibaldizhou.sagrada.graphics.view.listener.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController extends Controller implements Initializable {

    @FXML
    public StackPane rootPane;
    @FXML
    public Pane corePane;
    @FXML
    public Pane notifyPane;

    private DraftPoolListener draftPoolListener;
    private RoundTrackListener roundTrackListener;
    private StateListener stateListener;
    private GameListener gameListener;
    private DiceBagListener diceBagListener;
    private TimeoutListener timeoutListener;

    private IGameViewStrategy gameViewStrategy;

    private GameModel gameModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNotifyPane();
    }

    private void initListeners(){
        try {
            draftPoolListener = new DraftPoolListener(this, corePane, notifyPane);
            roundTrackListener = new RoundTrackListener(this, corePane, notifyPane,
                    new RoundTrackView(gameViewStrategy.getRoundTrackScale()));
            stateListener = new StateListener(this, corePane, notifyPane);
            gameListener = new GameListener(this, corePane, notifyPane);
            diceBagListener = new DiceBagListener(this, corePane, notifyPane);
            timeoutListener = new TimeoutListener(this, corePane, notifyPane);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    private void initNotifyPane() {
        notifyPane.setVisible(false);
        notifyPane.prefWidthProperty().bind(rootPane.widthProperty());
        notifyPane.prefHeightProperty().bind(rootPane.heightProperty());
        notifyPane.toBack();
    }

    public void initMultiPlayerGame(String token, String username, String gameName, ConnectionManager connectionManager) throws NetworkException {
        gameViewStrategy = new MultiPlayerGameViewStrategy(this, corePane, notifyPane);
        initListeners();
        gameModel = new GameModel(username, token, gameName, connectionManager);
        try {
            gameModel.joinGame(gameListener, gameListener, stateListener,
                    roundTrackListener, draftPoolListener, diceBagListener, timeoutListener);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    public void initSinglePlayerGame(String username, Difficulty difficulty, ConnectionManager connectionManager) throws NetworkException {
        gameViewStrategy = new SinglePlayerGameViewStrategy(this, corePane, notifyPane);
        initListeners();
        ClientCreateMessage builder = new ClientCreateMessage();
        ClientGetMessage parser = new ClientGetMessage();
        String request = builder.createUsernameMessage(username).createValueMessage(difficulty.getDifficulty()).buildMessage();
        String response = null;
        try {
            response = connectionManager.getGameController().createSinglePlayer(request);
            String token = parser.getToken(response);
            String gameName = parser.getGameName(response);
            gameModel = new GameModel(username, token, gameName, connectionManager);
            gameModel.joinGame(gameListener, gameListener, stateListener, roundTrackListener, draftPoolListener,
                    diceBagListener, timeoutListener);
        } catch (IOException e) {
            throw new NetworkException(e);
        }

    }

    public void setRoundTrack() {
        roundTrackListener.drawRoundTrack();
    }

    public void setDraftPool() {
        draftPoolListener.drawDraftPool();
    }

    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        gameModel.chooseSchemaCard(schemaCardWrapper);
    }

    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        return gameModel.getSchemaCardMap();
    }

    public String getUsername() {
        return gameModel.getUsername();
    }

    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        return gameModel.getOwnPrivateObjectiveCard();
    }

    public RoundTrackWrapper getRoundTrack() throws IOException {
        return gameModel.getRoundTrack();
    }

    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        gameModel.bindPlayer(user, playerObserver, schemaCardObserver);
    }

    public DraftPoolWrapper getDraftPool() throws IOException {
        return gameModel.getDraftPool();
    }

    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        gameModel.bindToolCard(toolCard, toolCardObserver);
    }

    public void endTurn() throws IOException {
        gameModel.endTurn();
    }

    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        List<UserWrapper> userWrappers = gameModel.getUserList();
        if (userWrappers.size() == 1) {
            Map<UserWrapper, Integer> fakeCoin = new HashMap<>();
            fakeCoin.putIfAbsent(userWrappers.get(0), -1);
            return fakeCoin;
        }
        return gameModel.getCoinsMap();
    }

    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        return gameModel.getOwnSchemaCard();
    }

    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        gameModel.placeDice(dice, positionWrapper);
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        return gameModel.getPublicObjectiveCards();
    }

    public List<ToolCardWrapper> getToolCards() throws IOException {
        return gameModel.getToolCards();
    }

    public List<UserWrapper> getUserList() throws IOException {
        return gameModel.getUserList();
    }

    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        gameModel.useToolCard(toolCardWrapper, executorObserver);
    }

    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        gameModel.sendDiceObject(diceWrapper);
    }

    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        gameModel.sendColorObject(colorWrapper);
    }

    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        gameModel.sendPositionObject(positionWrapper);
    }

    public void sendAnswerObject(boolean answer) throws IOException {
        gameModel.sendAnswerObject(answer);
    }

    public void sendValueObject(int value) throws IOException {
        gameModel.sendValueObject(value);
    }

    public void updateAllViews() throws IOException {
        draftPoolListener.updateView();
        roundTrackListener.updateView();
        gameListener.updateView();
        stateListener.updateView();
        timeoutListener.updateView();
        diceBagListener.updateView();
    }

    public int getOwnToken() throws IOException {
        return gameModel.getOwnToken();
    }

    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        return gameModel.getToolCardByName(toolCardWrapper);
    }

    public IGameViewStrategy getGameViewStrategy() {
        return gameViewStrategy;
    }

    public void destroyToolCard(ToolCardListener toolCardListener) {
        gameListener.destroyToolCard(toolCardListener);
    }

    public void choosePrivateObjectiveCard(PrivateObjectiveCardWrapper privateObjectiveCardWrapper) throws IOException {
        gameModel.choosePrivateObjectiveCard(privateObjectiveCardWrapper);
    }


}
