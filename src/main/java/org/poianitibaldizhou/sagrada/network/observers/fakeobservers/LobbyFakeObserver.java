package org.poianitibaldizhou.sagrada.network.observers.fakeobservers;

import org.poianitibaldizhou.sagrada.network.observers.LobbyObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.ILobbyFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.protocol.ServerCreateMessage;

import java.io.IOException;

public class LobbyFakeObserver implements ILobbyFakeObserver {

    private String token;
    private LobbyObserverManager observerManager;
    private ILobbyObserver realObserver;

    public LobbyFakeObserver(String token, ILobbyObserver realObserver, LobbyObserverManager observerManager) {
        this.token = token;
        this.realObserver = realObserver;
        this.observerManager = observerManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserJoin(User user) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onUserJoin(serverCreateMessage.createUserMessage(user).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserExit(User user) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onUserExit(serverCreateMessage.createUserMessage(user).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameStart(String gameName) {
        Runnable runnable = () -> {
            try {
                ServerCreateMessage serverCreateMessage = new ServerCreateMessage();
                realObserver.onGameStart(serverCreateMessage.createGameNameMessage(gameName).buildMessage());
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPing() {
        Runnable runnable = () -> {
            try {
                realObserver.onPing();
            } catch (IOException e) {
                observerManager.signalDisconnection(token);
            }
        };

        observerManager.pushThreadInQueue(token, runnable);
    }
}
