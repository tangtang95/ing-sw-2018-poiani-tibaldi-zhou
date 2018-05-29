package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.IGame;

import java.util.HashSet;
import java.util.Set;

public class ObserverManager {

    private Set<String> disconnectedObserver;
    private IGame game;

    public ObserverManager(String gameName, IGame game) {
        this.game = game;
        disconnectedObserver = new HashSet<>();
    }

    // GETTER

    public Set<String> getDisconnectedObserver() {
        return new HashSet<>(disconnectedObserver);
    }

    // MODIFIER

    public void signalDisconnection(String token) {
        disconnectedObserver.add(token);
    }

    public void signalReconnect(String token) {
        disconnectedObserver.remove(token);
    }
}
