package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

public class DiceView extends Pane {

    private ImageView diceImage;
    private DiceWrapper diceWrapper;
    private double scale;

    private boolean isMovable;

    private Point2D initialTranslate;
    private Point2D mousePosition;

    private static final String DICE_IMAGE_PATH = "images/board/dices.png";
    private static final String DICE_JSON_PATH = "images/board/dices.json";

    public DiceView(DiceWrapper dice, double scale) {
        this.diceWrapper = dice;
        this.scale = scale;
        String imageKey = String.format("dice-%s-%s.png", dice.getColor().name().toLowerCase(),
                String.valueOf(dice.getNumber()));
        diceImage = TextureUtils.getImageView(imageKey, DICE_IMAGE_PATH, DICE_JSON_PATH, scale);

        this.getChildren().add(diceImage);
        this.setOnMouseEntered(event -> {
            Glow glow = new Glow();
            glow.setLevel(0.6);
            diceImage.setEffect(glow);
        });
        this.setOnMouseExited(event -> {
            Glow glow = new Glow();
            glow.setLevel(0);
            diceImage.setEffect(glow);
        });

        setMovable(false);

    }

    public void setMovable(boolean movable){
        isMovable = movable;
        if(movable){
            // TODO FIX
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
        return diceImage.getFitWidth();
    }

    public double getImageHeight(){
        return diceImage.getFitHeight();
    }


    public DiceWrapper getDiceWrapper() {
        return diceWrapper;
    }

    public void reRoll(int number) {
        // TODO ANIMATION
        diceWrapper = new DiceWrapper(diceWrapper.getColor(), number);
        String imageKey = String.format("dice-%s-%s.png", diceWrapper.getColor().name().toLowerCase(),
                String.valueOf(diceWrapper.getNumber()));
        TextureUtils.changeViewport(diceImage, imageKey, DICE_JSON_PATH, scale);
    }
}
