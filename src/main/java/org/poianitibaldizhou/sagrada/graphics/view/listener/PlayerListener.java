package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.MultiPlayerController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.PlayerView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerListener extends AbstractView implements IPlayerObserver{

    private PlayerView playerView;

    protected PlayerListener(MultiPlayerController controller, Pane corePane, Pane notifyPane) throws RemoteException {
        super(controller, corePane, notifyPane);
    }

    @Override
    public void onFavorTokenChange(String value) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        Integer favorTokenUsed = parser.getValue(value);
        Platform.runLater(() -> {
            playerView.decreaseFavorToken(favorTokenUsed);
        });
    }

    @Override
    public void onSetOutcome(String outcome) throws IOException {
        /* NOT IMPORTANT FOR THE GUI*/
        Logger.getAnonymousLogger().log(Level.INFO, outcome);
    }
}
