package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.ToolCardView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;

public class ToolCardListener extends AbstractView implements IToolCardObserver {

    private transient ToolCardView toolCardView;

    protected ToolCardListener(ToolCardView toolCardView, MultiPlayerController controller,
                               Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
        DropShadow dropShadow = new DropShadow(4, 4, 4, Color.GRAY);
        this.toolCardView = toolCardView;
        toolCardView.setEffect(dropShadow);
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
        Platform.runLater(() -> corePane.getChildren().remove(toolCardView));
    }
}
