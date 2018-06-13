package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.ToolCardView;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;

import java.io.IOException;
import java.rmi.RemoteException;

public class ToolCardListener extends AbstractView implements IToolCardObserver {

    private transient ToolCardView toolCardView;
    private String toolCardName;

    public ToolCardListener(ToolCardView toolCardView, GameController controller,
                               Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.toolCardView = toolCardView;
        this.toolCardName = toolCardView.getToolCardWrapper().getName();
    }

    public ToolCardView getToolCardView() {
        return toolCardView;
    }

    @Override
    public void onTokenChange(String message) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        Integer value = parser.getValue(message);
        Platform.runLater(() -> {
            toolCardView.increaseToken(value);
        });
    }

    @Override
    public void onCardDestroy() throws IOException {
        Platform.runLater(() -> controller.destroyToolCard(this));
    }

    @Override
    public void updateView() {
        try {
            ToolCardWrapper toolCardWrapper = controller.getToolCardByName(toolCardView.getToolCardWrapper());
            toolCardView.redrawToken(toolCardWrapper.getToken());
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
            e.printStackTrace();
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ToolCardListener)) return false;
        return toolCardName.equals(((ToolCardListener) obj).toolCardName);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(toolCardName).hashCode();
    }
}
