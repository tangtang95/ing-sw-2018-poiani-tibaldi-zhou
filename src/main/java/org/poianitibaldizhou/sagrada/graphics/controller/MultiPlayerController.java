package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
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
import org.poianitibaldizhou.sagrada.graphics.view.*;
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
    public Pane canvasPane;

    private DraftPoolListener draftPoolView;
    private RoundTrackListener roundTrackView;
    private StateListener stateView;
    private GameListener gameView;
    private DiceBagListener diceBagView;

    private MultiPlayerModel multiPlayerModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            draftPoolView = new DraftPoolListener(new DraftPoolView());
            roundTrackView = new RoundTrackListener(new RoundTrackView());
            stateView = new StateListener(new StateView());
            gameView = new GameListener(new GameView());
            diceBagView = new DiceBagListener();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        /*testFrontBackSchemaCardView();
        testSchemaCardView();
        testDiceView();
        testRoundTrackView();
        testToolCardView();
        testPublicObjectiveCardView();
        testPrivateObjectiveCardView();*/
    }

    public void setMultiPlayerModel(String token, String username, String gameName, ConnectionManager connectionManager) {
        multiPlayerModel = new MultiPlayerModel(username, token, new GameModel(gameName), connectionManager);
        try {
            List<UserWrapper> users = multiPlayerModel.joinGame(gameView, gameView, stateView,
                    roundTrackView, draftPoolView, diceBagView);
            List<UserWrapper> usersOrdered = getUsersOrdered(users);
            System.out.println(users);
            System.out.println(usersOrdered.toString());
            drawUsers(usersOrdered);
        } catch (IOException e) {
            e.printStackTrace();
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato: " + e.toString());
        }

    }

    private List<UserWrapper> getUsersOrdered(final List<UserWrapper> users){
        List<UserWrapper> usersOrdered = new ArrayList<>();
        boolean userFounded = false;
        for (UserWrapper user: users) {
            if(user.getUsername().equals(multiPlayerModel.getUsername())) {
                userFounded = true;
            }
            if(userFounded){
                usersOrdered.add(user);
            }
        }
        for (UserWrapper user: users) {
            if(user.getUsername().equals(multiPlayerModel.getUsername())) {
                break;
            }
            usersOrdered.add(user);
        }
        return usersOrdered;
    }

    private void drawUsers(List<UserWrapper> users){
        double distance = canvasPane.getHeight()/3;
        double centerX = canvasPane.getWidth()/2;
        double centerY = canvasPane.getHeight()/2;

        for (int i = 0; i < users.size(); i++) {
            Pane pane = new Pane();
            pane.setPrefWidth(100);
            pane.setPrefHeight(100);
            pane.setStyle("-fx-border-color: black; -fx-border-width: 2em");

            double angle = 2*Math.PI*i / users.size();
            double offsetX = distance * Math.cos(angle);
            double offsetY = distance * Math.sin(angle);

            double x = centerX + offsetX;
            double y = centerY + offsetY;

            this.canvasPane.getChildren().add(pane);
            pane.setTranslateX(x - pane.getPrefWidth()/2);
            pane.setTranslateY(y - pane.getPrefHeight()/2);
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

            this.canvasPane.getChildren().add(privateObjectiveCardView);
            privateObjectiveCardView.setTranslateX(100);
            privateObjectiveCardView.setTranslateY(400);
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }

        PrivateObjectiveCardView retroView = new PrivateObjectiveCardView(0.3);
        this.canvasPane.getChildren().add(retroView);

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
            this.canvasPane.getChildren().add(publicObjectiveCardView);
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
        canvasPane.getChildren().add(toolCardView);
        toolCardView.setTranslateY(200);
    }

    private void testFrontBackSchemaCardView(){
        DrawableCollection<FrontBackSchemaCard> schemaCards = new DrawableCollection<>();
        GameInjector.injectSchemaCards(schemaCards);
        JSONObject frontSchema = null;
        JSONObject backSchema = null;
        try {
            FrontBackSchemaCard frontBackSchemaCard = schemaCards.draw();
            frontSchema = (JSONObject) frontBackSchemaCard.getFrontSchemaCard().toJSON().get("body");
            backSchema = (JSONObject) frontBackSchemaCard.getBackSchemaCard().toJSON().get("body");
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
        SchemaCardWrapper schemaCard = new SchemaCardWrapper("test", 3, new TileWrapper[4][5]);
        FrontBackSchemaCardView cardView = new FrontBackSchemaCardView((SchemaCardWrapper) schemaCard.toObject(frontSchema),
                (SchemaCardWrapper) schemaCard.toObject(backSchema), 0.3);
        cardView.flipCard(Duration.millis(5000));
        canvasPane.getChildren().add(cardView);
        cardView.setTranslateY(400);
        cardView.setTranslateX(700);
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
        canvasPane.getChildren().add(roundTrackView);
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

        canvasPane.getChildren().addAll(schemaCardView1);
        schemaCardView1.setTranslateX(300);
        schemaCardView1.setTranslateY(300);
    }

    private void testDiceView(){
        DiceWrapper dice = new DiceWrapper(ColorWrapper.BLUE, 2);
        DiceView diceView = new DiceView(dice, 0.1);

        canvasPane.getChildren().add(diceView);
        diceView.setTranslateX(300);
        diceView.setTranslateY(300);
    }



}
