package org.poianitibaldizhou.sagrada.graphics.view.listener;

import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IRoundTrackObserver;
import org.poianitibaldizhou.sagrada.graphics.view.RoundTrackView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RoundTrackListener extends UnicastRemoteObject implements IRoundTrackObserver {

    private final transient RoundTrackView roundTrackView;

    public RoundTrackListener(RoundTrackView roundTrackView) throws RemoteException {
        this.roundTrackView = roundTrackView;
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
