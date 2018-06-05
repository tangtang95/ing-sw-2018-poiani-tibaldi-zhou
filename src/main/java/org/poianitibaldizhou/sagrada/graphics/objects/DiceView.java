package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureJSONParser;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceView extends Pane {

    private ImageView diceView;

    private boolean isMovable;

    private Point2D initialTranslate;
    private Point2D mousePosition;

    public DiceView(DiceWrapper dice, double scale) {
        Image diceImage = new Image(getClass().getClassLoader().getResourceAsStream("images/board/dices.png"));
        diceView = new ImageView(diceImage);
        diceView.setPreserveRatio(true);
        JSONParser parser = new JSONParser();
        try {
            String value = String.valueOf(dice.getNumber());
            String color = dice.getColor().name().toLowerCase();
            TextureJSONParser textureParser = new TextureJSONParser("images/board/dices.json");
            Rectangle2D rectangle2D = textureParser.getRectangleView(String.format("dice-%s-%s.png", color, value));
            diceView.setViewport(rectangle2D);
            diceView.setFitWidth(rectangle2D.getWidth()*scale);
            diceView.setFitHeight(rectangle2D.getHeight()*scale);
        } catch (ParseException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot parse Dice jsonString");
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Cannot find files");
        }


        this.getChildren().add(diceView);
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

        setMovable(false);

    }

    public void setMovable(boolean movable){
        isMovable = movable;
        if(movable){
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
        else{
            this.setOnMousePressed(Event::consume);

            this.setOnMouseDragged(Event::consume);

            this.setOnDragDropped(Event::consume);
        }
    }

    public boolean isMovable() {
        return isMovable;
    }

    public double getImageWidth(){
        return diceView.getFitWidth();
    }

    public double getImageHeight(){
        return diceView.getFitHeight();
    }



}
