package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.MessageType;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ITimeOutObserver;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;


/**
 * OVERVIEW: Listen to the modification of the timeout s
 */
public class TimeoutListener extends AbstractView implements ITimeOutObserver {

    /**
     * Constructor.
     * Create a timeout listener that shows a message when the timeout is expired
     *
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public TimeoutListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        /* NOTHING TO UPDATE */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimeOut(String message) throws IOException {
        Platform.runLater(() -> showMessage(getActivePane(), ClientMessage.TIMEOUT_ERROR, MessageType.ERROR));
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
