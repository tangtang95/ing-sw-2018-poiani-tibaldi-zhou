package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
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
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.model.MultiPlayerModel;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.graphics.view.*;
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
        GameView gameView = new GameView(this, corePane, notifyPane);
        try {
            draftPoolView = new DraftPoolListener(new DraftPoolView());
            roundTrackView = new RoundTrackListener(new RoundTrackView());
            stateView = new StateListener(new StateView());
            gameListener = new GameListener(gameView);
            diceBagView = new DiceBagListener();
            timeoutListener = new TimeoutListener();
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
            List<UserWrapper> users = multiPlayerModel.joinGame(gameListener, gameListener, stateView,
                    roundTrackView, draftPoolView, diceBagView, timeoutListener);
        } catch (IOException e) {
            e.printStackTrace();
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato: " + e.toString());
        }

    }

    public void chooseSchemaCard(SchemaCardWrapper schemaCardWrapper) throws IOException {
        multiPlayerModel.chooseSchemaCard(schemaCardWrapper);
    }

    private void drawUsers(List<UserWrapper> users){

        double centerX = rootPane.getPrefWidth()/2;
        double centerY = rootPane.getPrefHeight()/2;

        Map<Integer, Double> angles = new HashMap<>();

        for (int i = 0; i < users.size(); i++) {


            Pane pane = new Pane();
            ImageView imageView = TextureUtils.getSimpleImageView("images/user.png", 0.15);
            imageView.translateXProperty().bind(pane.widthProperty().divide(2)
                    .subtract(imageView.fitWidthProperty().divide(2)));
            imageView.translateYProperty().bind(pane.heightProperty().divide(2)
                    .subtract(imageView.fitHeightProperty().divide(2)));
            pane.getChildren().add(imageView);

            pane.setStyle("-fx-border-color: black; -fx-border-width: 0.6em; -fx-border-radius: 1em");

            double angle = 2*Math.PI*i / ((users.size() == 3) ? users.size() + 1 : users.size()) - Math.PI/2;
            DoubleBinding distance = rootPane.heightProperty().divide(2)
                    .subtract(pane.heightProperty().divide(1.8));
            DoubleBinding offsetX = distance.multiply(Math.cos(angle));
            DoubleBinding offsetY = distance.multiply(Math.sin(angle));

            double tangentAngle = angle - Math.PI/2;
            DoubleBinding tangentDistance = rootPane.heightProperty().divide(3);
            DoubleBinding tangentOffsetX = tangentDistance.multiply(Math.cos(tangentAngle));
            DoubleBinding tangentOffsetY = tangentDistance.multiply(Math.sin(tangentAngle));

            DrawableCollection<FrontBackSchemaCard> schemaCards = new DrawableCollection<>();
            GameInjector.injectSchemaCards(schemaCards);
            JSONObject object = null;
            try {
                object = (JSONObject) schemaCards.draw().getFrontSchemaCard().toJSON().get("body");
            } catch (EmptyCollectionException e) {
                e.printStackTrace();
            }
            SchemaCardWrapper schemaCard = new SchemaCardWrapper("test", 3, new TileWrapper[4][5]);
            SchemaCardView schemaCardView1 = new SchemaCardView((SchemaCardWrapper) schemaCard.toObject(object), 0.2);
            schemaCardView1.setRotate(angle*180.0/Math.PI - 90);
            DoubleBinding distanceSchemaCard = rootPane.heightProperty().divide(2)
                    .subtract(schemaCardView1.heightProperty().divide(1.8));

            corePane.getChildren().addAll(schemaCardView1);
            schemaCardView1.translateXProperty().bind(schemaCardView1.widthProperty().divide(2)
                    .negate().add(centerX).add(distanceSchemaCard.multiply(Math.cos(angle))));
            schemaCardView1.translateYProperty().bind(schemaCardView1.heightProperty().divide(2)
                    .negate().add(centerY).add(distanceSchemaCard.multiply(Math.sin(angle))));

            DoubleBinding x = offsetX.add(centerX).add(tangentOffsetX);
            DoubleBinding y = offsetY.add(centerY).add(tangentOffsetY);

            this.corePane.getChildren().add(pane);
            pane.setRotate(angle*180.0/Math.PI - 90);
            pane.translateXProperty().bind(pane.widthProperty().divide(2).negate().add(x));
            pane.translateYProperty().bind(pane.heightProperty().divide(2).negate().add(y));
        }

    }

    private void testPrivateObjectiveCardView(){
        DrawableCollection<PrivateObjectiveCard> privateObjectiveCards = new DrawableCollection<>();
        GameInjector.injectPrivateObjectiveCard(privateObjectiveCards);

        try {
            JSONObject object = (JSONObject) privateObjectiveCards.draw().toJSON().get("body");
            PrivateObjectiveCardWrapper privateObjectiveCardWrapper =
                    new PrivateObjectiveCardWrapper("Sfumature Rosse - Privata", "dsad", ColorWrapper.BLUE);
            PrivateObjectiveCardView privateObjectiveCardView = new PrivateObjectiveCardView(
                    (PrivateObjectiveCardWrapper) privateObjectiveCardWrapper.toObject(object), 0.3
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

        GameView gameView = gameListener.getGameView();

        gameView.activateNotifyPane();
        gameView.clearNotifyPane();
        gameView.showPrivateObjectiveCards(privateObjectiveCardList);
        gameView.showFrontBackSchemaCards(frontBackSchemaCardWrappers);
    }

    private void testRoundTrackView(){
        List<Collection<DiceWrapper>> dices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<DiceWrapper> diceRound = new ArrayList<>();
            Random rng = new Random();
            diceRound.add(new DiceWrapper(ColorWrapper.values()[rng.nextInt(5)], rng.nextInt(6) + 1));
            diceRound.add(new DiceWrapper(ColorWrapper.BLUE, 3));
            dices.add(diceRound);
        }

        RoundTrackWrapper roundTrackWrapper = new RoundTrackWrapper(dices);
        RoundTrackView roundTrackView = new RoundTrackView();
        roundTrackView.drawDices(roundTrackWrapper);
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
}
