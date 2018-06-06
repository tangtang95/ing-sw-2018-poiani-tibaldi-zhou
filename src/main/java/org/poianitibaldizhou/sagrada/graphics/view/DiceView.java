package org.poianitibaldizhou.sagrada.graphics.view;

import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

public class DiceView extends Pane {

    private ImageView diceView;

    private boolean isMovable;

    private Point2D initialTranslate;
    private Point2D mousePosition;

    private static final String DICE_IMAGE_PATH = "images/board/dices.png";
    private static final String DICE_JSON_PATH = "images/board/dices.json";

    public DiceView(DiceWrapper dice, double scale) {
        String imageKey = String.format("dice-%s-%s.png", dice.getColor().name().toLowerCase(),
                String.valueOf(dice.getNumber()));
        diceView = TextureUtils.getImageView(imageKey, DICE_IMAGE_PATH, DICE_JSON_PATH, scale);

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
