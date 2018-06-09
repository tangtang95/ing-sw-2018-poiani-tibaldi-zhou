package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;

import java.io.IOException;

public class ToolCardView extends Pane{

    private final ImageView toolCardView;
    private ToolCardWrapper toolCardWrapper;
    private final double scale;
    private Canvas tokenView;

    //Based on toolCardWidth
    private static final double ICON_PERCENT_RADIUS = 0.14;

    private static final String CARD_IMAGE_PATH = "images/cards/tool-cards.png";
    private static final String CARD_JSON_PATH = "images/cards/tool-cards.json";


    public ToolCardView(ToolCardWrapper toolCard, double scale){
        this.toolCardWrapper = toolCard;
        this.scale = scale;
        String cardKey = TextureUtils.convertNameIntoCardKey(toolCard.getName());
        toolCardView = TextureUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);

        tokenView = drawToken(toolCard.getToken());
        tokenView.setTranslateX(toolCardView.getFitWidth() - tokenView.getWidth()/2);
        tokenView.setTranslateY(- tokenView.getHeight()/2);

        this.getChildren().addAll(toolCardView, tokenView);
    }

    public ToolCardView(ToolCardWrapper toolCard){
        this(toolCard, 1);
    }

    private Canvas drawToken(int numbersOfToken){
        Canvas canvas = new Canvas(toolCardView.getFitWidth()*ICON_PERCENT_RADIUS,
                toolCardView.getFitWidth()*ICON_PERCENT_RADIUS);
        double offset = canvas.getWidth()*0.1;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(offset/2,offset/2, canvas.getWidth() - offset,
                canvas.getHeight() - offset);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(canvas.getWidth()*0.5));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(String.valueOf(numbersOfToken), canvas.getWidth()/2,
                canvas.getHeight()/2);
        gc.strokeOval(offset/2,offset/2, canvas.getWidth() - offset,
                canvas.getHeight() - offset);
        return canvas;
    }

    public void increaseToken(Integer value) {
        toolCardWrapper = new ToolCardWrapper(toolCardWrapper.getName(),
                toolCardWrapper.getDescription(), toolCardWrapper.getColor(), toolCardWrapper.getToken() + value);
        this.getChildren().remove(tokenView);
        tokenView = drawToken(value);

        tokenView.setTranslateX(toolCardView.getFitWidth() - tokenView.getWidth()/2);
        tokenView.setTranslateY(- tokenView.getHeight()/2);

        this.getChildren().add(tokenView);
    }
}
