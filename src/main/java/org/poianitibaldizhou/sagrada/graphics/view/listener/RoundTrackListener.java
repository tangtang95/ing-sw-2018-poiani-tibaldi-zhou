package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.poianitibaldizhou.sagrada.graphics.view.IGameViewStrategy;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.utils.GraphicsUtils;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.DiceView;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.RoundTrackWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RoundTrackListener extends AbstractView implements IRoundTrackObserver {

    private transient RoundTrackView roundTrackView;
    private transient List<RoundTrackView> copyRoundTrackViews;

    private transient List<Pane> diceViews;
    private transient Label roundLabel;

    private static final double ROUND_TRACK_SCALE = 0.5;
    private static final double ROUND_TRACK_SHOW_SCALE = 1;
    private static final double DICE_SCALE = 0.3;

    public RoundTrackListener(GameController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        copyRoundTrackViews = new ArrayList<>();
        diceViews = new ArrayList<>();
        roundLabel = new Label();
        roundLabel.setFont(Font.font(ROUND_TRACK_SHOW_SCALE*30));
        roundLabel.setTextFill(Color.WHITE);

        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        roundTrackView = new RoundTrackView(ROUND_TRACK_SCALE);

        roundTrackView.setEffect(dropShadow);
        roundTrackView.getStyleClass().add("on-board-card");

        roundTrackView.setOnMousePressed(this::onRoundTrackPressed);
    }

    @Override
    public void updateView() {
        try {
            RoundTrackWrapper roundTrackWrapper = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrackWrapper);
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
            e.printStackTrace();
        }
    }

    public void onRoundTrackPressed(MouseEvent mouseEvent){
        clearNotifyPane(true);
        activateNotifyPane();

        drawSimpleCloseHelperBox(notifyPane, "Tracciato dei round");
        RoundTrackWrapper copyRoundTrack = roundTrackView.getRoundTrackWrapper();
        RoundTrackView copyRoundTrackView = new RoundTrackView(copyRoundTrack, ROUND_TRACK_SHOW_SCALE);
        copyRoundTrackViews.clear();
        copyRoundTrackViews.add(copyRoundTrackView);

        drawPane(notifyPane, copyRoundTrackView, "on-notify-pane-card",
                getPivotX(getCenterX(), copyRoundTrackView.widthProperty(), 0.5),
                getPivotY(getCenterY(), copyRoundTrackView.heightProperty(), 0));

        for (int i = 0; i < RoundTrackWrapper.NUMBER_OF_TRACK; i++) {
            int round = i;
            copyRoundTrackView.setDicePressedEvent(round, (event1 -> {
                notifyPane.getChildren().removeAll(diceViews);
                notifyPane.getChildren().removeAll(roundLabel);
                diceViews.clear();

                RoundTrackWrapper roundTrackWrapper = copyRoundTrackView.getRoundTrackWrapper();
                List<DiceWrapper> diceList = roundTrackWrapper.getDicesPerRound(round);
                diceList.forEach(diceWrapper -> diceViews.add(new DiceView(diceWrapper, DICE_SCALE)));
                DoubleBinding y = copyRoundTrackView.translateYProperty()
                        .add(copyRoundTrackView.heightProperty()).add(PADDING*3);
                roundLabel.setText(String.format("Round %s", String.valueOf(round)));
                roundLabel.translateXProperty().bind(getPivotX(getCenterX(), roundLabel.widthProperty(), 0.5));
                roundLabel.translateYProperty().bind(y);
                notifyPane.getChildren().add(roundLabel);

                GraphicsUtils.drawCenteredPanes(notifyPane, diceViews, "on-notify-pane-card",
                        getCenterX(), roundLabel.translateYProperty().add(roundLabel.heightProperty().add(PADDING*3)));
            }));
        }
        mouseEvent.consume();
    }

    public RoundTrackView getRoundTrackView() {
        return roundTrackView;
    }

    public void drawRoundTrack() {
        IGameViewStrategy gameViewStrategy = controller.getGameViewStrategy();
        roundTrackView.translateXProperty().bind(getPivotX(gameViewStrategy.getRoundTrackCenterX(),
                roundTrackView.widthProperty(), 0.5));
        roundTrackView.translateYProperty().bind(getPivotY(gameViewStrategy.getRoundTrackCenterY(),
                roundTrackView.heightProperty(), 0.5));
        corePane.getChildren().add(roundTrackView);
    }

    @Override
    public void onDicesAddToRound(String message) throws IOException {
        Platform.runLater(this::updateRoundTrack);
    }

    @Override
    public void onDiceAddToRound(String message) throws IOException {
        Platform.runLater(this::updateRoundTrack);
    }

    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {
        Platform.runLater(this::updateRoundTrack);
    }

    @Override
    public void onDiceSwap(String message) throws IOException {
        Platform.runLater(this::updateRoundTrack);
    }

    private void updateRoundTrack(){
        try {
            RoundTrackWrapper roundTrack = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrack);
            copyRoundTrackViews.forEach(trackView -> trackView.drawRoundTrack(roundTrack));
        } catch (IOException e) {
            e.printStackTrace();
            showCrashErrorMessage("errore di connessione");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RoundTrackListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }


}
