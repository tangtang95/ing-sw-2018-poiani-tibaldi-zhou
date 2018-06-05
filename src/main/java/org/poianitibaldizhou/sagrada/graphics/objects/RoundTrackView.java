package org.poianitibaldizhou.sagrada.graphics.objects;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.RoundTrackWrapper;

import java.util.Optional;

public class RoundTrackView extends Pane {

    private ImageView roundTrackView;

    //Based on image width
    private static final double FIRST_OFFSET_X_PERCENT = 0.04;

    //Based on image width
    private static final double OFFSET_X_PERCENT = 0.02;
    //Based on image height
    private static final double OFFSET_Y_PERCENT = 0.565;

    //based on image width
    private static final double ICON_PERCENT_RADIUS = 0.3;

    private static final double DICE_SCALE = 0.15;

    public RoundTrackView(RoundTrackWrapper roundTrack){
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/board/round-track.png"));
        roundTrackView = new ImageView(image);
        roundTrackView.setPreserveRatio(true);
        this.getChildren().add(roundTrackView);
        for (int i = 0; i < roundTrack.size(); i++) {
            Optional<DiceWrapper> diceOptional =roundTrack.getDicesPerRound(i).stream().findFirst();
            final int round = i;
            diceOptional.ifPresent((dice) -> drawDices(dice, round, image.getWidth(),
                    image.getHeight(), roundTrack.getDicesPerRound(round).size()));
        }
    }

    private void drawDices(DiceWrapper dice, int round, double imageWidth, double imageHeight, int numberOfDices){
        long firstOffsetX = Math.round(FIRST_OFFSET_X_PERCENT*imageWidth);
        long offsetX = Math.round(OFFSET_X_PERCENT*imageWidth);
        long offsetY = Math.round(OFFSET_Y_PERCENT*imageHeight);

        DiceView diceView = new DiceView(dice, DICE_SCALE);
        diceView.setTranslateX(firstOffsetX + diceView.getImageWidth()*round + offsetX*round);
        diceView.setTranslateY(offsetY);
        this.getChildren().add(diceView);

        drawNumberOfDicesIcon(diceView, numberOfDices);
    }

    private void drawNumberOfDicesIcon(DiceView diceView, int numberOfDices){
        Canvas canvas = new Canvas(diceView.getImageWidth()*ICON_PERCENT_RADIUS*1.5,
                diceView.getImageHeight()*ICON_PERCENT_RADIUS*1.5);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(0,0, diceView.getImageWidth()*ICON_PERCENT_RADIUS,
                diceView.getImageHeight()*ICON_PERCENT_RADIUS);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.strokeText(String.valueOf(numberOfDices), diceView.getImageWidth()*ICON_PERCENT_RADIUS/2,
                diceView.getImageHeight()*ICON_PERCENT_RADIUS/2);
        gc.strokeOval(0,0,diceView.getImageWidth()*ICON_PERCENT_RADIUS,
                diceView.getImageHeight()*ICON_PERCENT_RADIUS);
        canvas.setTranslateX(diceView.getImageWidth() - diceView.getImageWidth()*ICON_PERCENT_RADIUS/1.5);
        canvas.setTranslateY(diceView.getImageHeight() -  diceView.getImageHeight()*ICON_PERCENT_RADIUS/1.5);
        diceView.getChildren().add(canvas);
    }



}
