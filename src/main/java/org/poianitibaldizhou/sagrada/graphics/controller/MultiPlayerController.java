package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.model.MultiPlayerModel;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.graphics.view.listener.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class MultiPlayerController extends Controller implements Initializable {

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

    private MultiPlayerModel multiPlayerModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNotifyPane();
        try {
            draftPoolListener = new DraftPoolListener(this, corePane, notifyPane);
            roundTrackListener = new RoundTrackListener(this, corePane, notifyPane);
            stateListener = new StateListener(this, corePane, notifyPane);
            gameListener = new GameListener(this, corePane, notifyPane);
            diceBagListener = new DiceBagListener(this, corePane, notifyPane);
            timeoutListener = new TimeoutListener(this, corePane, notifyPane);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void initNotifyPane() {
        notifyPane.setVisible(false);
        notifyPane.prefWidthProperty().bind(rootPane.widthProperty());
        notifyPane.prefHeightProperty().bind(rootPane.heightProperty());
        notifyPane.toBack();
    }

    public void setMultiPlayerModel(String token, String username, String gameName, ConnectionManager connectionManager) {
        multiPlayerModel = new MultiPlayerModel(username, token, new GameModel(gameName), connectionManager);
        try {
            multiPlayerModel.joinGame(gameListener, gameListener, stateListener,
                    roundTrackListener, draftPoolListener, diceBagListener, timeoutListener);
        } catch (IOException e) {
            e.printStackTrace();
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato: " + e.toString());
        }

    }

    public void setRoundTrack() {
        roundTrackListener.drawRoundTrack();
    }

    public void setDraftPool() {
        draftPoolListener.drawDraftPool();
    }

    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        multiPlayerModel.chooseSchemaCard(schemaCardWrapper);
    }

    public Map<UserWrapper, SchemaCardWrapper> getSchemaCardMap() throws IOException {
        return multiPlayerModel.getSchemaCardMap();
    }

    public String getUsername() {
        return multiPlayerModel.getUsername();
    }

    public List<PrivateObjectiveCardWrapper> getOwnPrivateObjectiveCard() throws IOException {
        return multiPlayerModel.getOwnPrivateObjectiveCard();
    }

    public RoundTrackWrapper getRoundTrack() throws IOException {
        return multiPlayerModel.getRoundTrack();
    }

    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) throws IOException {
        multiPlayerModel.bindPlayer(user, playerObserver, schemaCardObserver);
    }

    public DraftPoolWrapper getDraftPool() throws IOException {
        return multiPlayerModel.getDraftPool();
    }

    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) throws IOException {
        multiPlayerModel.bindToolCard(toolCard, toolCardObserver);
    }

    public void endTurn() throws IOException {
        multiPlayerModel.endTurn();
    }

    public Map<UserWrapper, Integer> getCoinsMap() throws IOException {
        return multiPlayerModel.getCoinsMap();
    }

    public SchemaCardWrapper getOwnSchemaCard() throws IOException {
        return multiPlayerModel.getOwnSchemaCard();
    }

    public void placeDice(DiceWrapper dice, PositionWrapper positionWrapper) throws IOException {
        multiPlayerModel.placeDice(dice, positionWrapper);
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards() throws IOException {
        return multiPlayerModel.getPublicObjectiveCards();
    }

    public List<ToolCardWrapper> getToolCards() throws IOException {
        return multiPlayerModel.getToolCards();
    }

    public List<UserWrapper> getUserList() throws IOException {
        return multiPlayerModel.getUserList();
    }

    public void useToolCard(ToolCardWrapper toolCardWrapper, IToolCardExecutorObserver executorObserver) throws IOException {
        multiPlayerModel.useToolCard(toolCardWrapper, executorObserver);
    }

    public void sendDiceObject(DiceWrapper diceWrapper) throws IOException {
        multiPlayerModel.sendDiceObject(diceWrapper);
    }

    public void sendColorObject(ColorWrapper colorWrapper) throws IOException {
        multiPlayerModel.sendColorObject(colorWrapper);
    }

    public void sendPositionObject(PositionWrapper positionWrapper) throws IOException {
        multiPlayerModel.sendPositionObject(positionWrapper);
    }

    public void sendAnswerObject(boolean answer) throws IOException {
        multiPlayerModel.sendAnswerObject(answer);
    }

    public void sendValueObject(int value) throws IOException {
        multiPlayerModel.sendValueObject(value);
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
        return multiPlayerModel.getOwnToken();
    }

    public ToolCardWrapper getToolCardByName(ToolCardWrapper toolCardWrapper) throws IOException {
        return multiPlayerModel.getToolCardByName(toolCardWrapper);
    }
}
