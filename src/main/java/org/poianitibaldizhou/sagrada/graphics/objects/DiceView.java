package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureJSONParser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceView extends Pane {

    private ImageView diceView;

    private Point2D initialTranslate;
    private Point2D mousePosition;

    public DiceView(String jsonString) {
        Image diceImage = new Image(getClass().getClassLoader().getResourceAsStream("images/board/dices.png"));
        diceView = new ImageView(diceImage);
        JSONParser parser = new JSONParser();
        try {
            JSONObject dice = (JSONObject) parser.parse(jsonString);
            String value = String.valueOf(dice.get("value"));
            String color = (String) dice.get("color");
            TextureJSONParser textureParser = new TextureJSONParser("images/board/dices.json");
            this.diceView.setViewport(textureParser.getRectangleView(String.format("dice-%s-%s.png", color.toLowerCase(), value)));
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot parse Dice jsonString");
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find files");
        }
        this.getChildren().add(diceView);
        this.setScaleX(0.3);
        this.setScaleY(0.3);
        this.setOnMouseEntered(event -> {
            Glow glow = new Glow();
            glow.setLevel(0.6);
            diceView.setEffect(glow);
        });
        this.setOnMouseExited(event -> {
            Glow glow = new Glow();
            glow.setLevel(0);
            diceView.setEffect(glow);
        });
        this.setOnMousePressed(event -> {
            mousePosition = new Point2D(event.getSceneX(), event.getSceneY());
            initialTranslate = new Point2D(this.getTranslateX(), this.getTranslateY());
        });

        this.setOnMouseDragged(event -> {
            double mouseX = event.getSceneX();
            double mouseY = event.getSceneY();
            this.setLayoutX(mouseX - mousePosition.getX());
            this.setLayoutY(mouseY - mousePosition.getY());
            event.consume();
        });

        this.setOnDragDropped(event -> {
            mousePosition = new Point2D(event.getSceneX(), event.getSceneY());
        });
    }



}