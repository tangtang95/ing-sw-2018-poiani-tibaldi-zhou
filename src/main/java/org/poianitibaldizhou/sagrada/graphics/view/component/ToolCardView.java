package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;

/**
 * OVERVIEW: Represents the view for a tool card
 */
public class ToolCardView extends Pane{

    private final ImageView imageView;
    private ToolCardWrapper toolCardWrapper;
    private Canvas tokenView;

    //Based on toolCardWidth
    private static final double ICON_PERCENT_RADIUS = 0.14;

    private static final String CARD_IMAGE_PATH = "src/test/images/cards/tool-cards.png";
    private static final String CARD_JSON_PATH = "src/test/images/cards/tool-cards.json";


    /**
     * Constructor.
     * Create a ToolCardView (pane) that contains the toolCard given
     *
     * @param toolCard the model of the toolCard to draw
     * @param scale the scale value
     */
    public ToolCardView(ToolCardWrapper toolCard, double scale){
        this.toolCardWrapper = toolCard;
        String cardKey = GraphicsUtils.convertNameIntoCardKey(toolCard.getName());
        imageView = GraphicsUtils.getImageView(cardKey + ".png", CARD_IMAGE_PATH, CARD_JSON_PATH, scale);

        tokenView = drawToken(toolCard.getToken());
        tokenView.setTranslateX(imageView.getFitWidth() - tokenView.getWidth()/2);
        tokenView.setTranslateY(- tokenView.getHeight()/2);

        this.getChildren().addAll(imageView, tokenView);
    }

    /**
     * Re-draw the token of the tool card view with the value passed
     *
     * @param token the new value of the token
     */
    public void redrawToken(int token) {
        this.getChildren().remove(tokenView);
        tokenView = drawToken(token);
        tokenView.setTranslateX(imageView.getFitWidth() - tokenView.getWidth()/2);
        tokenView.setTranslateY(- tokenView.getHeight()/2);

        this.getChildren().add(tokenView);
    }

    /**
     * Increase the value of the token inside the token image
     * @param value the value to increase
     */
    public void increaseToken(Integer value) {
        toolCardWrapper = new ToolCardWrapper(toolCardWrapper.getName(),
                toolCardWrapper.getDescription(), toolCardWrapper.getColor(), toolCardWrapper.getToken() + value);
        this.getChildren().remove(tokenView);
        tokenView = drawToken(toolCardWrapper.getToken());

        tokenView.setTranslateX(imageView.getFitWidth() - tokenView.getWidth()/2);
        tokenView.setTranslateY(- tokenView.getHeight()/2);

        this.getChildren().add(tokenView);
    }

    /**
     * Return the tool card that is viewed with this class
     * @return
     */
    public ToolCardWrapper getToolCardWrapper() {
        return toolCardWrapper;
    }

    /**
     * Draw the token on the tool card
     *
     * @param numbersOfToken number of tokens to draw
     * @return token that has been drawn
     */
    private Canvas drawToken(int numbersOfToken){
        Canvas canvas = new Canvas(imageView.getFitWidth()*ICON_PERCENT_RADIUS,
                imageView.getFitWidth()*ICON_PERCENT_RADIUS);
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


}
