package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.RoundTrackView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RoundTrackListener extends AbstractView implements IRoundTrackObserver {

    private transient RoundTrackView roundTrackView;

    public RoundTrackListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    public RoundTrackView getRoundTrackView() {
        return roundTrackView;
    }

    @Override
    public void onDicesAddToRound(String message) throws IOException {

    }

    @Override
    public void onDiceAddToRound(String message) throws IOException {

    }

    @Override
    public void onDiceRemoveFromRound(String message) throws IOException {

    }

    @Override
    public void onDiceSwap(String message) throws IOException {

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
