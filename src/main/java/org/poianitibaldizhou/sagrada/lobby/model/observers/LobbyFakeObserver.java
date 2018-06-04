package org.poianitibaldizhou.sagrada.lobby.model.observers;

import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.protocol.JSONServerProtocol;

import java.io.IOException;

public class LobbyFakeObserver implements ILobbyFakeObserver {

    private String token;
    private LobbyObserverManager observerManager;
    private ILobbyObserver realObserver;

    private JSONServerProtocol serverNetworkProtocol;

    public LobbyFakeObserver(String token, ILobbyObserver realObserver, LobbyObserverManager observerManager) {
        this.token = token;
        this.realObserver = realObserver;
        this.observerManager = observerManager;

        serverNetworkProtocol = new JSONServerProtocol();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserJoin(User user) {
        Runnable runnable = () -> {
            try {
                realObserver.onUserJoin(serverNetworkProtocol.appendMessage(user));
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
                realObserver.onUserExit(serverNetworkProtocol.appendMessage(user));
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
                realObserver.onGameStart(serverNetworkProtocol.appendMessage(gameName));
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
