package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.model.MultiPlayerModel;
import org.poianitibaldizhou.sagrada.graphics.objects.*;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MultiPlayerController extends Controller implements Initializable {

    @FXML
    public Pane canvasPane;

    private GraphicsContext graphicsContext;

    private MultiPlayerModel multiPlayerModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        testFrontBackSchemaCardView();
        testSchemaCardView();
        testDiceView();
        testRoundTrackView();
        testToolCardView();
    }

    public void setMultiPlayerModel(String token, String username, String gameName, ConnectionManager connectionManager) {
        multiPlayerModel = new MultiPlayerModel(username, token, new GameModel(gameName), connectionManager);
        try {
            multiPlayerModel.joinGame();
        } catch (IOException e) {
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato");
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
