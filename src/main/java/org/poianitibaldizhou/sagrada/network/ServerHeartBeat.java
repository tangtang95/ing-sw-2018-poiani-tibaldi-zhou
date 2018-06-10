package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;

/**
 * OVERVIEW: Heart beat for detection client disconnection
 */
public class ServerHeartBeat extends Thread {

    private final String gameName;
    private final GameManager gameManager;

    private LobbyObserverManager lobbyObserverManager;

    private final long SLEEP_TIME = ServerSettings.getHearthBeatMillis();

    /**
     * Constructor.
     * Creates a new server heart beat for detecting client disconnection
     * related to a certain game.
     *
     * @param gameManager game manager of the server
     * @param gameName name of the game on which the heart beat acts
     */
    public ServerHeartBeat(GameManager gameManager, String gameName) {
        this.gameName = gameName;
        this.gameManager = gameManager;
    }

    /**
     * Heart beats the observers of the client for detecting disconnections
     */
    @Override
    public void run() {
        while(true) {
            try {
                synchronized (gameName) {
                    // TODO
                }
            } catch(NullPointerException npe) {
                if(!gameManager.notContainsGame(gameName))
                    throw new IllegalStateException();
            }
        }
    }
}
