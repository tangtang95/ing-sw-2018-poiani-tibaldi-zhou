package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;

import java.io.IOException;
import java.rmi.RemoteException;

public class SchemaCardListener extends AbstractView implements ISchemaCardObserver{

    protected SchemaCardListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onPlaceDice(String message) throws IOException {

    }

    @Override
    public void onDiceRemove(String message) throws IOException {

    }
}
