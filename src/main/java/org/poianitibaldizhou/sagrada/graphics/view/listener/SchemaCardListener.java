package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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

    private transient SchemaCardView schemaCardView;

    protected SchemaCardListener(SchemaCardView schemaCardView, MultiPlayerController controller,
                                 Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        this.schemaCardView = schemaCardView;
        schemaCardView.setEffect(dropShadow);

    }

    @Override
    public void onPlaceDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            schemaCardView.drawDice(diceWrapper, positionWrapper);
            if(getActivePane() == notifyPane){
                clearNotifyPane();
                deactivateNotifyPane();
            }
        });
    }

    @Override
    public void onDiceRemove(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            try {
                schemaCardView.removeDice(diceWrapper, positionWrapper);
            } catch (IOException e) {
                showCrashErrorMessage("Errore di sincronismo");
            }
        });
    }
}
