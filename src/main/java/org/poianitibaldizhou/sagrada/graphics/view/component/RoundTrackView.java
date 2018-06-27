package org.poianitibaldizhou.sagrada.graphics.view.component;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.RoundTrackWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoundTrackView extends Pane{

    private final ImageView roundTrackImage;
    private final double scale;

    private final List<DiceView> diceViewList;
    private RoundTrackWrapper roundTrack;

    //Based on component width
    private static final double FIRST_OFFSET_X_PERCENT = 0.04;

    //Based on component width
    private static final double OFFSET_X_PERCENT = 0.02;
    //Based on component height
    private static final double OFFSET_Y_PERCENT = 0.565;

    //based on dice width
    private static final double ICON_PERCENT_RADIUS = 0.6;

    private static final double DICE_SCALE = 0.3;

    /**
     * Constructor.
     * Create an empty RoundTrackView (pane) without any dice
     *
     * @param scale
     */
    public RoundTrackView(double scale){
        this.scale = scale;
        diceViewList = new ArrayList<>();
        roundTrack = new RoundTrackWrapper(new ArrayList<>());
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/board/round-track.png"));
        roundTrackImage = new ImageView(image);
        roundTrackImage.setPreserveRatio(true);
        roundTrackImage.setFitWidth(image.getWidth()*scale);
        roundTrackImage.setFitHeight(image.getHeight()*scale);
        this.getChildren().add(roundTrackImage);
    }

    /**
     * Constructor.
     * Create a RoundTrackView (pane) that contains every diceView inside the roundTrack passed
     *
     * @param roundTrack the model of the roundTrack
     * @param scale the scale value
     */
    public RoundTrackView(RoundTrackWrapper roundTrack, double scale){
        this.scale = scale;
        diceViewList = new ArrayList<>();
        Image image = new Image(getClass().getClassLoader().getResourceAsStream("images/board/round-track.png"));
        roundTrackImage = new ImageView(image);
        roundTrackImage.setPreserveRatio(true);
        roundTrackImage.setFitWidth(image.getWidth()*scale);
        roundTrackImage.setFitHeight(image.getHeight()*scale);
        this.getChildren().add(roundTrackImage);

        drawRoundTrack(roundTrack);
    }

    /**
     * Draw the dices of the RoundTrack
     *
     * @param roundTrack the model of the roundTrack to draw
     */
    public void drawRoundTrack(RoundTrackWrapper roundTrack) {
        this.roundTrack = roundTrack;
        clearDices();
        drawDices(roundTrack);
    }

    /**
     * @param round the number of the round to get dices from
     * @return a list of dices that are inside the round passed
     */
    public List<DiceWrapper> getDices(int round){
        return roundTrack.getDicesForRound(round);
    }

    /**
     * @return the model of the roundTrack used to draw it
     */
    public RoundTrackWrapper getRoundTrackWrapper() {
        return roundTrack;
    }

    /**
     * Add an event handler (onMousePressed) to the dice of the chosen round
     *
     * @param round the chosen round
     * @param eventHandler the event to add
     */
    public void setDicePressedEvent(int round, EventHandler<? super MouseEvent> eventHandler) {
        if(diceViewList.size() <= round)
            return;
        diceViewList.get(round).setOnMousePressed(eventHandler);
    }

    private void drawDices(RoundTrackWrapper roundTrack){

        for (int i = 0; i < RoundTrackWrapper.NUMBER_OF_TRACK; i++) {
            Optional<DiceWrapper> diceOptional = roundTrack.getDicesForRound(i).stream().findFirst();
            final int round = i;
            diceOptional.ifPresent(dice -> drawDice(dice, round, roundTrackImage.getFitWidth(),
                    roundTrackImage.getFitHeight(), roundTrack.getDicesForRound(round).size()));
        }
    }

    private void drawDice(DiceWrapper dice, int round, double imageWidth, double imageHeight, int numberOfDices){
        long firstOffsetX = Math.round(FIRST_OFFSET_X_PERCENT*imageWidth);
        long offsetX = Math.round(OFFSET_X_PERCENT*imageWidth);
        long offsetY = Math.round(OFFSET_Y_PERCENT*imageHeight);

        DiceView diceView = new DiceView(dice, scale*DICE_SCALE);
        diceView.setTranslateX(firstOffsetX + diceView.getImageWidth()*round + offsetX*round);
        diceView.setTranslateY(offsetY);
        this.getChildren().add(diceView);
        diceViewList.add(diceView);

        drawNumberOfDicesIcon(diceView, numberOfDices);
    }

    private void drawNumberOfDicesIcon(DiceView diceView, int numberOfDices) {
        // TODO REFACTOR there are duplicates
        Canvas canvas = new Canvas(diceView.getImageWidth() * ICON_PERCENT_RADIUS * 1.5,
                diceView.getImageHeight() * ICON_PERCENT_RADIUS * 1.5);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillOval(0, 0, diceView.getImageWidth() * ICON_PERCENT_RADIUS,
                diceView.getImageHeight() * ICON_PERCENT_RADIUS);
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(ICON_PERCENT_RADIUS*diceView.getImageWidth()));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(String.valueOf(numberOfDices), diceView.getImageWidth() * ICON_PERCENT_RADIUS / 2,
                diceView.getImageHeight() * ICON_PERCENT_RADIUS / 2);
        gc.strokeOval(0, 0, diceView.getImageWidth() * ICON_PERCENT_RADIUS,
                diceView.getImageHeight() * ICON_PERCENT_RADIUS);
        canvas.setTranslateX(diceView.getImageWidth() - diceView.getImageWidth() * ICON_PERCENT_RADIUS / 1.5);
        canvas.setTranslateY(diceView.getImageHeight() - diceView.getImageHeight() * ICON_PERCENT_RADIUS / 1.5);
        diceView.getChildren().add(canvas);
    }

    private void clearDices(){
        diceViewList.forEach(diceView -> this.getChildren().remove(diceView));
    }

}
