package org.poianitibaldizhou.sagrada.game.model;

public class TerminationGameManager {

    private final String gameName;
    private final GameManager gameManager;

    public TerminationGameManager(String gameName, GameManager gameManager) {
        this.gameName = gameName;
        this.gameManager = gameManager;
    }

    public void terminateGame() {
        gameManager.terminateGame(gameName);
    }
}
