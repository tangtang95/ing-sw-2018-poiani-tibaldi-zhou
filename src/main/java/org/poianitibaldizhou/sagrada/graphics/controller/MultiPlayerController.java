package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.poianitibaldizhou.sagrada.graphics.model.GameModel;
import org.poianitibaldizhou.sagrada.graphics.model.MultiPlayerModel;
import org.poianitibaldizhou.sagrada.graphics.objects.DiceView;
import org.poianitibaldizhou.sagrada.graphics.objects.RoundTrackView;
import org.poianitibaldizhou.sagrada.graphics.objects.SchemaCardView;
import org.poianitibaldizhou.sagrada.graphics.utils.AlertBox;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ColorWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.RoundTrackWrapper;

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

        testSchemaCardView();
        testDiceView();
        testRoundTrackView();
    }

    public void setMultiPlayerModel(String token, String username, String gameName, ConnectionManager connectionManager) {
        multiPlayerModel = new MultiPlayerModel(username, token, new GameModel(gameName), connectionManager);
        try {
            multiPlayerModel.joinGame();
        } catch (IOException e) {
            AlertBox.displayBox("Errore di rete", "Sagrada è crashato");
        }
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
        RoundTrackView roundTrackView = new RoundTrackView(roundTrackWrapper);
        canvasPane.getChildren().add(roundTrackView);
        roundTrackView.setTranslateX(400);
    }

    private void testSchemaCardView() {
        SchemaCardView schemaCardView = new SchemaCardView("", 0.3);
        SchemaCardView schemaCardView1 = new SchemaCardView("", 0.3);
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), schemaCardView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setByAngle(90);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        rotator.play();
        rotator.setOnFinished(event -> {
            schemaCardView.setScaleX(0.7);
            schemaCardView.setScaleY(0.7);
            RotateTransition rotator1 = new RotateTransition(Duration.millis(1000), schemaCardView);
            rotator1.setAxis(Rotate.Y_AXIS);
            rotator1.setByAngle(90);
            rotator1.setInterpolator(Interpolator.LINEAR);
            rotator1.setCycleCount(1);
            rotator1.play();
        });

        canvasPane.getChildren().addAll(schemaCardView, schemaCardView1);
        schemaCardView1.setTranslateX(300);
    }

    private void testDiceView(){
        DiceWrapper dice = new DiceWrapper(ColorWrapper.BLUE, 2);
        DiceView diceView = new DiceView(dice, 0.1);

        canvasPane.getChildren().add(diceView);
        diceView.setTranslateX(300);
        diceView.setTranslateY(300);
    }



}
