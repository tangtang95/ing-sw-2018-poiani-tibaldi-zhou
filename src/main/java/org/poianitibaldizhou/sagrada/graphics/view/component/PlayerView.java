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

    public PlayerView(UserWrapper userWrapper, int favorTokens, double fontSize) {
        this.fontSize = fontSize;
        this.favorTokens = favorTokens;
        nameLabel = new Label(userWrapper.getUsername());
        nameLabel.setFont(Font.font(fontSize));
        drawFavorTokens(favorTokens);

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

    public PlayerView(UserWrapper userWrapper, int favorTokens) {
        this(userWrapper, favorTokens, 10);
    }

    public void decreaseFavorToken(Integer favorTokenUsed) {
        favorTokens -= favorTokenUsed;
        this.getChildren().remove(coinCanvas);
        drawFavorTokens(favorTokens);
    }
}
