package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

public class PlayerView extends Pane {

    private Label nameLabel;
    private Canvas coinCanvas;
    private int favorTokens;
    private double fontSize;

    /**
     * Constructor.
     * Create a PlayerView (pane) that contains a label for the player name and an image that represents
     * the favor tokens
     *
     * @param userWrapper the model of the user
     * @param favorTokens the number of favor tokens
     * @param fontSize the size of the label font
     */
    public PlayerView(UserWrapper userWrapper, int favorTokens, double fontSize) {
        this(userWrapper, fontSize);
        this.favorTokens = favorTokens;
        drawFavorTokens(favorTokens);
    }

    /**
     * Constructor.
     * Create a PlayerView (pane) that contains a label (with standard size = 10) for the player name and an image
     * that represents the favor tokens
     *
     * @param userWrapper the model of the user
     * @param favorTokens the number of favor tokens
     */
    public PlayerView(UserWrapper userWrapper, int favorTokens) {
        this(userWrapper, favorTokens, 10);
    }

    /**
     * Constructor.
     * Create a PlayerView (pane) that contains a label (with standard size = 10) for the player name
     *
     * @param userWrapper the model of the user
     * @param fontSize the size of the label font
     */
    public PlayerView(UserWrapper userWrapper, double fontSize){
        this.fontSize = fontSize;
        nameLabel = new Label(userWrapper.getUsername());
        nameLabel.setFont(Font.font(fontSize));
        nameLabel.getStyleClass().add("player-label");

        this.getChildren().add(nameLabel);
    }

    private void drawFavorTokens(int favorTokens) {
        coinCanvas = new Canvas(fontSize * 1.5, fontSize * 1.5);
        GraphicsContext gc = coinCanvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(0, 0, fontSize * 1.5, fontSize * 1.5);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(fontSize));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.strokeText(String.valueOf(favorTokens), coinCanvas.getWidth() / 2, coinCanvas.getHeight() / 2);
        gc.strokeOval(0, 0, fontSize * 1.5, fontSize * 1.5);
        coinCanvas.translateXProperty().bind(nameLabel.widthProperty().add(coinCanvas.widthProperty().divide(3)));
        coinCanvas.translateYProperty().bind(nameLabel.heightProperty().subtract(coinCanvas.heightProperty()));
        this.getChildren().add(coinCanvas);
    }

    /**
     * Update the favor token image by decreasing its value by the number passed
     *
     * @param favorTokenUsed the decreasing value number
     */
    public void decreaseFavorToken(Integer favorTokenUsed) {
        favorTokens -= favorTokenUsed;
        this.getChildren().remove(coinCanvas);
        drawFavorTokens(favorTokens);
    }

    /**
     * Draw favor token image
     *
     * @param token the number of favor token
     */
    public void drawFavorToken(int token) {
        favorTokens = token;
        this.getChildren().remove(coinCanvas);
        drawFavorTokens(favorTokens);
    }
}
