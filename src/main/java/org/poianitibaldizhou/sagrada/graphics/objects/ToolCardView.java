package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.graphics.utils.TextureJSONParser;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolCardView extends Pane implements IToolCardObserver{



    private final ImageView toolCardView;
    private double toolCardWidth;
    private double toolCardHeight;

    private Canvas tokenView;

    //Based on toolCardWidth
    private static final double FONT_PERCENT_SCALE = 0.2;
    private static final double ICON_PERCENT_RADIUS = 0.05;

    public ToolCardView(ToolCardWrapper toolCard, double scale){
        Image toolCardImage = new Image(getClass().getClassLoader().getResourceAsStream("images/cards/tool-cards.png"));
        toolCardView = new ImageView(toolCardImage);


        try {
            TextureJSONParser textureParser = new TextureJSONParser("images/cards/tool-cards.json");
            String toolCardKey = toolCard.getName().replace(" ", "");
            toolCardKey = toolCardKey.substring(0, 1).toLowerCase() + toolCardKey.substring(1);
            Rectangle2D rectangleView = textureParser.getRectangleView(toolCardKey + ".png");
            toolCardWidth = rectangleView.getWidth()*scale;
            toolCardHeight = rectangleView.getHeight()*scale;
            toolCardView.setViewport(rectangleView);
            toolCardView.setFitWidth(toolCardWidth);
            toolCardView.setFitHeight(toolCardHeight);
        } catch (ParseException | IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }

        tokenView = drawToken(toolCard.getToken());
        tokenView.setTranslateX(toolCardWidth - tokenView.getWidth()/4);
        tokenView.setTranslateY(- tokenView.getHeight()/4);

        this.getChildren().addAll(toolCardView, tokenView);
    }

    public ToolCardView(ToolCardWrapper toolCard){
        this(toolCard, 1);
    }

    private Canvas drawToken(int numbersOfToken){
        Canvas canvas = new Canvas(toolCardWidth*ICON_PERCENT_RADIUS + 10,
                toolCardWidth*ICON_PERCENT_RADIUS + 10);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(5,5, toolCardWidth*ICON_PERCENT_RADIUS - 5,
                toolCardWidth*ICON_PERCENT_RADIUS - 5);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(toolCardWidth*FONT_PERCENT_SCALE));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(String.valueOf(numbersOfToken), toolCardWidth*ICON_PERCENT_RADIUS/2,
                toolCardWidth*ICON_PERCENT_RADIUS/2);
        gc.strokeOval(5,5,toolCardWidth*ICON_PERCENT_RADIUS - 5,
                toolCardWidth*ICON_PERCENT_RADIUS - 5);
        return canvas;
    }

    @Override
    public void onTokenChange(String tokens) throws IOException {

    }

    @Override
    public void onCardDestroy() throws IOException {

    }
}
