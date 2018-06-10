package org.poianitibaldizhou.sagrada.game.model;

/**
 * OVERVIEW: Manager for the termination of the game. It allows, when the game
 * terminate following its normal intercourse, to remove the game from the
 * game manager
 */
public class TerminationGameManager {

    private final String gameName;
    private final GameManager gameManager;

    /**
     * Constructor.
     * Creates a termination game manager for a certain game
     *
     * @param gameName handle the termination of the game with gameName as name
     * @param gameManager server game manager
     */
    public TerminationGameManager(String gameName, GameManager gameManager) {
        this.gameName = gameName;
        this.gameManager = gameManager;
    }

    /**
     * Terminates the game, thus remove it from the list of the game currently
     * played
     */
    public void terminateGame() {
        gameManager.terminateGame(gameName);
    }
}
