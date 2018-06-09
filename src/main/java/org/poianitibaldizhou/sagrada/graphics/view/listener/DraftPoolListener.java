package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDraftPoolObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.DraftPoolView;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DraftPoolWrapper;

import java.io.IOException;
import java.rmi.RemoteException;

public class DraftPoolListener extends AbstractView implements IDraftPoolObserver{

    private transient DraftPoolView draftPoolView;

    public DraftPoolListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    public DraftPoolView getDraftPoolView() {
        return draftPoolView;
    }

    public void drawDraftPool() {

    }

    @Override
    public void onDiceAdd(String message) throws IOException {
        Platform.runLater(() -> {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawDraftPool(draftPoolWrapper);
        });
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        Platform.runLater(() -> {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawDraftPool(draftPoolWrapper);
        });
    }

    @Override
    public void onDicesAdd(String message) throws IOException {
        Platform.runLater(() -> {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawRollingDraftPool(draftPoolWrapper);
        });
    }

    @Override
    public void onDraftPoolReroll(String message) throws IOException {
        Platform.runLater(() -> {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawRollingDraftPool(draftPoolWrapper);
        });
    }

    @Override
    public void onDraftPoolClear() throws IOException {
        Platform.runLater(() -> {
            DraftPoolWrapper draftPoolWrapper = controller.getDraftPool();
            draftPoolView.drawDraftPool(draftPoolWrapper);
        });
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
