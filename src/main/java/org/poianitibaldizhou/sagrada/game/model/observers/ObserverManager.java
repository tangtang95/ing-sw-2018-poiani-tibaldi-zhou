package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.IGame;

import java.util.HashSet;
import java.util.Set;

public class ObserverManager {

    private Set<String> disconnectedPlayer;
    private IGame game;

    public ObserverManager(String gameName, IGame game) {
        this.game = game;
        disconnectedPlayer = new HashSet<>();
    }

    // GETTER

    public Set<String> getDisconnectedPlayer() {
        return new HashSet<>(disconnectedPlayer);
    }

    // MODIFIER

    public void signalDisconnection(String token) {
        disconnectedPlayer.add(token);
    }

    public void signalReconnect(String token) {
        disconnectedPlayer.remove(token);
    }
}
