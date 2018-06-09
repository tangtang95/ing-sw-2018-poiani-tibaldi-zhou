package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.RoundTrackWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RoundTrackListener extends AbstractView implements IRoundTrackObserver {

    private transient RoundTrackView roundTrackView;

    private static final double ROUND_TRACK_SCALE = 0.5;

    public RoundTrackListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    public RoundTrackView getRoundTrackView() {
        return roundTrackView;
    }

    public void drawRoundTrack() {
        roundTrackView = new RoundTrackView(ROUND_TRACK_SCALE);
        roundTrackView.translateXProperty().bind(getPivotX(getCenterX(), roundTrackView.widthProperty(), 0.5));
        roundTrackView.translateYProperty().bind(getPivotY(getCenterY(), roundTrackView.heightProperty(), 0.5));
        corePane.getChildren().add(roundTrackView);
    }

    @Override
    public void onDicesAddToRound(String message) throws IOException {
        Platform.runLater(() -> {
            RoundTrackWrapper roundTrack = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrack);
        });
    }

    @Override
    public void onDiceAddToRound(String message) throws IOException {
        Platform.runLater(() -> {
            RoundTrackWrapper roundTrack = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrack);
        });
    }

    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {
        Platform.runLater(() -> {
            RoundTrackWrapper roundTrack = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrack);
        });
    }

    @Override
    public void onDiceSwap(String message) throws IOException {
        Platform.runLater(() -> {
            RoundTrackWrapper roundTrack = controller.getRoundTrack();
            roundTrackView.drawRoundTrack(roundTrack);
        });
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
