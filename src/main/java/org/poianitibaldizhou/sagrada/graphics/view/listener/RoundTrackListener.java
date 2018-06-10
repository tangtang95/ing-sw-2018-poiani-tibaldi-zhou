package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
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
        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        roundTrackView = new RoundTrackView(ROUND_TRACK_SCALE);
        roundTrackView.translateXProperty().bind(getPivotX(getCenterX(), roundTrackView.widthProperty(), 0.5));
        roundTrackView.translateYProperty().bind(getPivotY(getCenterY(), roundTrackView.heightProperty(), 0.5));
        roundTrackView.setEffect(dropShadow);
    }

    public RoundTrackView getRoundTrackView() {
        return roundTrackView;
    }

    public void drawRoundTrack() {
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
