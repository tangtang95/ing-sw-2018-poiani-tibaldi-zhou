package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ITimeOutObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TimeoutListener extends AbstractView implements ITimeOutObserver {

    public TimeoutListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onTimeOut(String message) throws IOException {
        Platform.runLater(() -> {
            showMessage(getActivePane(), "Scaduto il tempo", MessageType.ERROR);
        });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TimeoutListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}
