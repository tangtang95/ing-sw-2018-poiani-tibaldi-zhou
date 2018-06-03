package org.poianitibaldizhou.sagrada.graphics.controller;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.graphics.objects.DiceView;
import org.poianitibaldizhou.sagrada.graphics.objects.SchemaCardView;

import java.net.URL;
import java.util.ResourceBundle;

public class MultiGameController extends Controller implements Initializable {


    @FXML
    public Pane canvasPane;

    private GraphicsContext graphicsContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Canvas canvas = new Canvas(canvasPane.getPrefWidth(), canvasPane.getPrefHeight());
        canvasPane.getChildren().add(canvas);
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(20,20, 50, 50);

        testSchemaCardView();
        testDiceView();
    }

    private void testSchemaCardView() {
        SchemaCardView schemaCardView = new SchemaCardView("");
        SchemaCardView schemaCardView1 = new SchemaCardView("");
        RotateTransition rotator = new RotateTransition(Duration.millis(1000), schemaCardView);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setByAngle(90);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(1);
        rotator.play();
        rotator.setOnFinished(event -> {
            schemaCardView.setScaleX(0.5);
            schemaCardView.setScaleY(0.5);
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
        Dice dice = new Dice(6, org.poianitibaldizhou.sagrada.game.model.Color.RED);
        DiceView diceView = new DiceView(((JSONObject)dice.toJSON().get("body")).toJSONString());

        canvasPane.getChildren().add(diceView);
        diceView.setTranslateX(300);
        diceView.setTranslateY(300);
    }



}
