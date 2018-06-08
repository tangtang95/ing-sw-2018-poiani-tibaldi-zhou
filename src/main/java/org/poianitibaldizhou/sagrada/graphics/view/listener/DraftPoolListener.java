package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.DraftPoolView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DraftPoolListener extends AbstractView implements IDraftPoolObserver{

    private transient DraftPoolView draftPoolView;

    public DraftPoolListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    public DraftPoolView getDraftPoolView() {
        return draftPoolView;
    }

    @Override
    public void onDiceAdd(String dice) throws IOException {

    }

    @Override
    public void onDiceRemove(String dice) throws IOException {

    }

    @Override
    public void onDicesAdd(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolReroll(String dices) throws IOException {

    }

    @Override
    public void onDraftPoolClear() throws IOException {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DraftPoolListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}
