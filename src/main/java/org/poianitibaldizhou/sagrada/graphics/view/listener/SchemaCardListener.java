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
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchemaCardListener extends AbstractView implements ISchemaCardObserver{

    private final transient SchemaCardView schemaCardView;
    private final String username;

    /**
     * Constructor.
     * Create a schema card listener that update its schemaCardView every time an update is called
     *
     * @param schemaCardView the schemaCardView to update
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @param userWrapper the user linked to the schemaCardView
     * @throws RemoteException network error
     */
    public SchemaCardListener(SchemaCardView schemaCardView, GameGraphicsController controller,
                                 Pane corePane, Pane notifyPane, UserWrapper userWrapper) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.username = userWrapper.getUsername();
        this.schemaCardView = schemaCardView;
        this.schemaCardView.getStyleClass().add("on-board-schema-card");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        try {
            Map<UserWrapper, SchemaCardWrapper> schemaCardWrapperMap = controller.getSchemaCardMap();
            SchemaCardWrapper schemaCardWrapper = schemaCardWrapperMap.get(new UserWrapper(username));
            schemaCardView.drawSchemaCard(schemaCardWrapper);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDiceRemove(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        PositionWrapper positionWrapper = parser.getPosition(message);
        DiceWrapper diceWrapper = parser.getDice(message);
        Platform.runLater(() -> {
            try {
                schemaCardView.removeDice(diceWrapper, positionWrapper);
            } catch (IOException e) {
                showCrashErrorMessage(ClientMessage.SYNCHRONIZED_ERROR);
            }
        });
    }

    public SchemaCardView getSchemaCardView() {
        return schemaCardView;
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
