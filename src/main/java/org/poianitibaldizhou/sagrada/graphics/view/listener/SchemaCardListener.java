package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ISchemaCardObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
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
    private String username;

    public SchemaCardListener(SchemaCardView schemaCardView, GameGraphicsController controller,
                                 Pane corePane, Pane notifyPane, UserWrapper userWrapper) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.username = userWrapper.getUsername();
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
            SchemaCardWrapper schemaCardWrapper = schemaCardWrapperMap.get(new UserWrapper(username));
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
        return this.username.equals(schemaCardListener.username);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }
}
