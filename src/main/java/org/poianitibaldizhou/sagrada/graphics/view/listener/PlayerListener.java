package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.PlayerView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerListener extends AbstractView implements IPlayerObserver {

    private final transient PlayerView playerView;
    private final String username;

    public PlayerListener(PlayerView playerView, GameController controller, Pane corePane, Pane notifyPane, UserWrapper userWrapper) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.playerView = playerView;
        this.username = userWrapper.getUsername();
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

    @Override
    public void updateView() {
        try {
            int token = controller.getOwnToken();
            playerView.drawFavorToken(token);
        } catch (IOException e) {
            showCrashErrorMessage("Errore di connessione");
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof PlayerListener)) return false;

        return username.equals(((PlayerListener) obj).username);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }
}

