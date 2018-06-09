package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;

import java.io.IOException;
import java.rmi.RemoteException;

public class SchemaCardListener extends AbstractView implements ISchemaCardObserver{

    private SchemaCardView schemaCardView;

    protected SchemaCardListener(SchemaCardView schemaCardView, MultiPlayerController controller,
                                 Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.schemaCardView = schemaCardView;
    }

    @Override
    public void onPlaceDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            schemaCardView.drawDice(diceWrapper, positionWrapper);
        });
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            schemaCardView.removeDice(diceWrapper, positionWrapper);
        });
    }
}
