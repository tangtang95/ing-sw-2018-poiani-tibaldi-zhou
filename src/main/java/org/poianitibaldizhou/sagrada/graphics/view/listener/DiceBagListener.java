package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IDrawableCollectionObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiceBagListener extends AbstractView implements IDrawableCollectionObserver {

    /**
     * Constructor.
     * Create a dice bag listener (black-box)
     *
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @throws RemoteException network error
     */
    public DiceBagListener(GameGraphicsController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementAdd(String elem) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onElementsAdd(String elemList) throws IOException {
        /*NOT IMPORTANT FOR THE GUI*/
    }

    /**
     * {@inheritDoc}
     */
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
