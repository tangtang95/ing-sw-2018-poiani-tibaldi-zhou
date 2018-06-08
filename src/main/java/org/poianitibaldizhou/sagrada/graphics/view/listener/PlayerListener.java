package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;

public class PlayerListener extends AbstractView implements IPlayerObserver{

    protected PlayerListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onFavorTokenChange(String value) throws IOException {

    }

    @Override
    public void onSetOutcome(String outcome) throws IOException {

    }
}
