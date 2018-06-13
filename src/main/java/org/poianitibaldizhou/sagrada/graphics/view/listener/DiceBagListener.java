package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceBagListener extends AbstractView implements IDrawableCollectionObserver {

    public DiceBagListener(GameController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void updateView() {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    @Override
    public void onElementAdd(String elem) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    @Override
    public void onElementsAdd(String elemList) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    @Override
    public void onElementDraw(String elem) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
        Logger.getAnonymousLogger().log(Level.INFO, "JSON: {0}", elem);
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
