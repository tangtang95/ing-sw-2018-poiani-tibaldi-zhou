package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.SchemaCardView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.DiceWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.PositionWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public class SchemaCardListener extends AbstractView implements ISchemaCardObserver{

    private transient SchemaCardView schemaCardView;
    private transient UserWrapper user;

    protected SchemaCardListener(SchemaCardView schemaCardView, MultiPlayerController controller,
                                 Pane corePane, Pane notifyPane, UserWrapper userWrapper) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.user = userWrapper;
        this.schemaCardView = schemaCardView;
        this.schemaCardView.getStyleClass().add("on-board-schema-card");
    }

    @Override
    public void onPlaceDice(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            schemaCardView.drawDice(diceWrapper, positionWrapper);
            if(getActivePane() == notifyPane){
                clearNotifyPane(false);
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

    public SchemaCardView getView() {
        return schemaCardView;
    }

    @Override
    public void updateView() {
        try {
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap = controller.getSchemaCardMap();
            SchemaCardWrapper schemaCardWrapper = schemaCardWrapperMap.get(user.getUsername());
            schemaCardView.drawSchemaCard(schemaCardWrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof SchemaCardListener))
            return false;
        SchemaCardListener schemaCardListener = (SchemaCardListener) obj;
        return this.user.equals(schemaCardListener.user);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(user.getUsername()).hashCode();
    }
}
