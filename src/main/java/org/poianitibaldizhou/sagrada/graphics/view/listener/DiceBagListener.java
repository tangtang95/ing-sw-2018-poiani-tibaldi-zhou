package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DiceBagListener extends AbstractView implements IDrawableCollectionObserver {

    public DiceBagListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onElementAdd(String elem) throws IOException {

    }

    @Override
    public void onElementsAdd(String elemList) throws IOException {

    }

    @Override
    public void onElementDraw(String elem) throws IOException {

    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DiceBagListener;
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}
