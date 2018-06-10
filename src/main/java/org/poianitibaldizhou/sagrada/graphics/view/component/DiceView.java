package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;

public class DiceView extends Pane {

    private ImageView diceImage;
    private DiceWrapper diceWrapper;
    private double scale;

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

    public Image getImage() {
        String imageKey = String.format("dice-%s-%s.png", diceWrapper.getColor().name().toLowerCase(),
                String.valueOf(diceWrapper.getNumber()));
        return TextureUtils.getImage("images/dices/", imageKey, diceImage.getFitWidth(), diceImage.getFitHeight());
    }
}
