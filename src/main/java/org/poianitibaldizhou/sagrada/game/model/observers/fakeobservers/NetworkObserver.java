package org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces.INetworkFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.INetworkObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;

public class NetworkObserver implements INetworkFakeObserver{

    private final String token;
    private final GameObserverManager gameObserverManager;
    private final INetworkObserver realObserver;

    private final ServerCreateMessage serverCreateMessage;

    /**
     * Creates a network fake observer
     *
     * @param token token of the real observer
     * @param gameObserverManager game observer manager for the game
     * @param networkObserver network real observer that needs to be notified
     */
    public NetworkObserver(String token, GameObserverManager gameObserverManager, INetworkObserver networkObserver) {
        this.token = token;
        this.gameObserverManager = gameObserverManager;
        this.realObserver = networkObserver;

        serverCreateMessage = new ServerCreateMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDisconnection(User user) {
        Runnable runnable = () -> {
            try {
                realObserver.signalDisconnection(serverCreateMessage.createUserMessage(user).buildMessage());
            } catch (IOException e) {
                gameObserverManager.signalDisconnection(token);
            }
        };

        gameObserverManager.pushThreadInQueue(token, runnable);
    }
}
