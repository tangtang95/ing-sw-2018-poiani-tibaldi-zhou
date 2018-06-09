package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.model.MultiPlayerModel;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.graphics.view.component.*;
import org.poianitibaldizhou.sagrada.graphics.view.listener.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiPlayerController extends Controller implements Initializable {

    @FXML
    public StackPane rootPane;
    @FXML
    public Pane corePane;
    @FXML
    public Pane notifyPane;

    private DraftPoolListener draftPoolView;
    private RoundTrackListener roundTrackView;
    private StateListener stateView;
    private GameListener gameListener;
    private DiceBagListener diceBagView;
    private TimeoutListener timeoutListener;

    private MultiPlayerModel multiPlayerModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initNotifyPane();
        try {
            draftPoolView = new DraftPoolListener(this, corePane, notifyPane);
            roundTrackView = new RoundTrackListener(this, corePane, notifyPane);
            stateView = new StateListener(this, corePane, notifyPane);
            gameListener = new GameListener(this, corePane, notifyPane);
            diceBagView = new DiceBagListener(this, corePane, notifyPane);
            timeoutListener = new TimeoutListener(this, corePane, notifyPane);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //testFrontBackSchemaCardView();
        /*testSchemaCardView();
        testDiceView();
        testRoundTrackView();
        testToolCardView();
        testPublicObjectiveCardView();
        testPrivateObjectiveCardView();*/
    }

    private void initNotifyPane(){
        notifyPane.setVisible(false);
        notifyPane.prefWidthProperty().bind(rootPane.widthProperty());
        notifyPane.prefHeightProperty().bind(rootPane.heightProperty());
        notifyPane.toBack();
    }

    public void setMultiPlayerModel(String token, String username, String gameName, ConnectionManager connectionManager) {
        multiPlayerModel = new MultiPlayerModel(username, token, new GameModel(gameName), connectionManager);
        try {
            multiPlayerModel.joinGame(gameListener, gameListener, stateView,
                    roundTrackView, draftPoolView, diceBagView, timeoutListener);
        } catch (IOException e) {
            e.printStackTrace();
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato: " + e.toString());
        }

    }

    public void setRoundTrack() {
        roundTrackView.drawRoundTrack();
    }

    public void setDraftPool() {
        draftPoolView.drawDraftPool();
    }

    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        multiPlayerModel.chooseSchemaCard(schemaCardWrapper);
    }

    private void testPrivateObjectiveCardView(){
        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        GameInjector.injectPrivateObjectiveCard(privateObjectiveCards);

        try {
            JSONObject object = (JSONObject) privateObjectiveCards.draw().toJSON().get("body");
            PrivateObjectiveCardWrapper privateObjectiveCardWrapper =
                    new PrivateObjectiveCardWrapper("Sfumature Rosse - Privata", "dsad", ColorWrapper.BLUE);
            PrivateObjectiveCardView privateObjectiveCardView =
                    new PrivateObjectiveCardView(privateObjectiveCardWrapper.toObject(object), 0.3
            );

            this.corePane.getChildren().add(privateObjectiveCardView);
            privateObjectiveCardView.setTranslateX(100);
            privateObjectiveCardView.setTranslateY(400);
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }

        PrivateObjectiveCardView retroView = new PrivateObjectiveCardView(0.3);
        this.corePane.getChildren().add(retroView);

    }

    private void testPublicObjectiveCardView(){
        DrawableCollection<PublicObjectiveCard> publicObjectiveCards = new DrawableCollection<>();
        GameInjector.injectPublicObjectiveCards(publicObjectiveCards);

        try {
            JSONObject object = (JSONObject) publicObjectiveCards.draw().toJSON().get("body");
            PublicObjectiveCardWrapper publicObjectiveCardWrapper =
                    new PublicObjectiveCardWrapper("Varietà di Colore", "dsad", 1);
            PublicObjectiveCardView publicObjectiveCardView = new PublicObjectiveCardView(
                    publicObjectiveCardWrapper, 0.5);
            this.corePane.getChildren().add(publicObjectiveCardView);
            publicObjectiveCardView.setTranslateX(600);
            publicObjectiveCardView.setTranslateY(300);
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    private void testToolCardView(){
        ToolCard toolCard = new ToolCard(Color.PURPLE, "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1." +
                        " Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][8-CA]"
        );

        JSONObject object = (JSONObject) toolCard.toJSON().get("body");
        ToolCardWrapper toolCardWrapper = new ToolCardWrapper("dasd", "dasd", ColorWrapper.BLUE, 3);
        ToolCardView toolCardView = new ToolCardView((ToolCardWrapper) toolCardWrapper.toObject(object), 0.6);
        corePane.getChildren().add(toolCardView);
        toolCardView.setTranslateY(200);
    }

    private void testFrontBackSchemaCardView(){
        DrawableCollection<FrontBackSchemaCard> schemaCards = new DrawableCollection<>();
        GameInjector.injectSchemaCards(schemaCards);

        List<FrontBackSchemaCardWrapper> frontBackSchemaCardWrappers = new ArrayList<>();
        JSONObject firstSchema = null;
        JSONObject secondSchema = null;
        try {
            FrontBackSchemaCard frontBackSchemaCard = schemaCards.draw();
            firstSchema = (JSONObject) frontBackSchemaCard.toJSON().get("body");
            frontBackSchemaCardWrappers.add(FrontBackSchemaCardWrapper.toObject(firstSchema));
            frontBackSchemaCard = schemaCards.draw();
            secondSchema = (JSONObject) frontBackSchemaCard.toJSON().get("body");
            frontBackSchemaCardWrappers.add(FrontBackSchemaCardWrapper.toObject(secondSchema));
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }

        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        GameInjector.injectPrivateObjectiveCard(privateObjectiveCards);
        List<PrivateObjectiveCardWrapper> privateObjectiveCardList = new ArrayList<>();

        try {
            JSONObject object = (JSONObject) privateObjectiveCards.draw().toJSON().get("body");
            PrivateObjectiveCardWrapper privateObjectiveCardWrapper =
                    new PrivateObjectiveCardWrapper("Sfumature Rosse - Privata", "dsad", ColorWrapper.BLUE);
            privateObjectiveCardList.add(privateObjectiveCardWrapper);
        }catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    private void testRoundTrackView(){
        List<List<DiceWrapper>> dices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<DiceWrapper> diceRound = new ArrayList<>();
            Random rng = new Random();
            diceRound.add(new DiceWrapper(ColorWrapper.values()[rng.nextInt(5)], rng.nextInt(6) + 1));
            diceRound.add(new DiceWrapper(ColorWrapper.BLUE, 3));
            dices.add(diceRound);
        }

        RoundTrackWrapper roundTrackWrapper = new RoundTrackWrapper(dices);
        RoundTrackView roundTrackView = new RoundTrackView();
        roundTrackView.drawRoundTrack(roundTrackWrapper);
        corePane.getChildren().add(roundTrackView);
        roundTrackView.setTranslateX(400);
    }

    private void testSchemaCardView() {
        DrawableCollection<FrontBackSchemaCard> schemaCards = new DrawableCollection<>();
        GameInjector.injectSchemaCards(schemaCards);
        JSONObject object = null;
        try {
             object = (JSONObject) schemaCards.draw().getFrontSchemaCard().toJSON().get("body");
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
        SchemaCardWrapper schemaCard = new SchemaCardWrapper("test", 3, new TileWrapper[4][5]);
        SchemaCardView schemaCardView1 = new SchemaCardView((SchemaCardWrapper) schemaCard.toObject(object), 0.3);

        corePane.getChildren().addAll(schemaCardView1);
        schemaCardView1.setTranslateX(300);
        schemaCardView1.setTranslateY(300);
    }

    private void testDiceView(){
        DiceWrapper dice = new DiceWrapper(ColorWrapper.BLUE, 2);
        DiceView diceView = new DiceView(dice, 0.1);

        corePane.getChildren().add(diceView);
        diceView.setTranslateX(300);
        diceView.setTranslateY(300);
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

    public RoundTrackWrapper getRoundTrack() {
        RoundTrackWrapper roundTrackWrapper;
        try {
            roundTrackWrapper =  multiPlayerModel.getRoundTrack();
        } catch (IOException e) {
            showCrashErrorMessage();
            return null;
        }
        return roundTrackWrapper;
    }

    private void showCrashErrorMessage() {
        Logger.getAnonymousLogger().log(Level.SEVERE, "Game crashed");
        // TODO close program and show AlertBox
    }

    public void bindPlayer(UserWrapper user, IPlayerObserver playerObserver, ISchemaCardObserver schemaCardObserver) {
        try {
            multiPlayerModel.bindPlayer(user, playerObserver, schemaCardObserver);
        } catch (IOException e) {
           showCrashErrorMessage();
        }
    }

    public DraftPoolWrapper getDraftPool() {
        DraftPoolWrapper draftPoolWrapper;
        try {
            draftPoolWrapper = multiPlayerModel.getDraftPool();
        } catch (IOException e) {
            showCrashErrorMessage();
            return null;
        }
        return draftPoolWrapper;
    }

    public void bindToolCard(ToolCardWrapper toolCard, IToolCardObserver toolCardObserver) {
        try {
            multiPlayerModel.bindToolCard(toolCard, toolCardObserver);
        } catch (IOException e) {
            showCrashErrorMessage();
        }
    }

    public void endTurn() {
        try {
            multiPlayerModel.endTurn();
        } catch (IOException e) {
            showCrashErrorMessage();
        }
    }
}
