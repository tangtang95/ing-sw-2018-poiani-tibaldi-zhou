package org.poianitibaldizhou.sagrada.graphics.view.listener;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IPlayerObserver;
import org.poianitibaldizhou.sagrada.graphics.controller.GameGraphicsController;
import org.poianitibaldizhou.sagrada.graphics.view.AbstractView;
import org.poianitibaldizhou.sagrada.graphics.view.component.PlayerView;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OVERVIEW: Listen to the modification of a player
 */
public class PlayerListener extends AbstractView implements IPlayerObserver {

    private final transient PlayerView playerView;
    private final String username;

    /**
     * Constructor.
     * Create a player listener that update its player view when there are notify from the server
     *
     * @param playerView the player view to update
     * @param controller the game controller of the GUI
     * @param corePane the core view of the game
     * @param notifyPane the view of the game to show the image on a greater size
     * @param userWrapper the model of the user related to the playerView
     * @throws RemoteException network error
     */
    public PlayerListener(PlayerView playerView, GameGraphicsController controller, Pane corePane, Pane notifyPane, UserWrapper userWrapper) throws RemoteException {
        super(controller, corePane, notifyPane);
        this.playerView = playerView;
        this.username = userWrapper.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFavorTokenChange(String value) throws IOException {
        ClientGetMessage parser = new ClientGetMessage();
        Integer favorTokenUsed = parser.getValue(value);
        Platform.runLater(() -> playerView.decreaseFavorToken(favorTokenUsed));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetOutcome(String outcome) throws IOException {
        /* NOT IMPORTANT FOR THE GUI*/
        Logger.getAnonymousLogger().log(Level.INFO, outcome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateView() {
        try {
            int token = controller.getOwnToken();
            playerView.drawFavorToken(token);
        } catch (IOException e) {
            showCrashErrorMessage(ClientMessage.CONNECTION_ERROR);
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof PlayerListener && username.equals(((PlayerListener) obj).username);

    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().concat(username).hashCode();
    }
}

