package org.poianitibaldizhou.sagrada.network;

import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.ServerSettings;
import org.poianitibaldizhou.sagrada.game.model.GameManager;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.game.model.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.game.view.IGameView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyObserverManager;

import java.util.Map;

/**
 * OVERVIEW: Heart beat for detection client disconnection
 */
public class ServerGameHeartBeat extends Thread {

    private final String gameName;
    private final GameManager gameManager;
    private final NetworkManager networkManager;
    private static final long SLEEP_TIME = ServerSettings.getHearthBeatMillis();

    /**
     * Constructor.
     * Creates a new server heart beat for detecting client disconnection
     * related to a certain game.
     *
     * @param gameManager game manager of the server
     * @param gameName name of the game on which the heart beat acts
     */
    public ServerGameHeartBeat(GameManager gameManager, String gameName, NetworkManager networkManager) {
        this.gameName = gameName;
        this.gameManager = gameManager;
        this.networkManager = networkManager;
    }

    /**
     * Heart beats the observers of the client for detecting disconnections
     */
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(SLEEP_TIME);
                try {
                    if (gameManager.clearObservers(gameName, networkManager))
                        break;
                } catch(NullPointerException npe) {
                    if(!gameManager.notContainsGame(gameName))
                        throw new IllegalStateException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
