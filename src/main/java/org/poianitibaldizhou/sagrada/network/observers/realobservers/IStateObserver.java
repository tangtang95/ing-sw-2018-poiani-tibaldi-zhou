package org.poianitibaldizhou.sagrada.network.observers.realobservers;

import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.rmi.Remote;
import java.util.Map;

public interface IStateObserver extends Remote {

    /**
     * Notify the start of setup game state
     *
     * @throws IOException network error
     */
    void onSetupGame() throws IOException;

    /**
     * Notify the start of setup player state
     *
     * @throws IOException network error
     */
    void onSetupPlayer() throws IOException;

    /**
     * Notify the start of round
     *
     * @param message message containing the current round and the user who has the dice bag
     * @throws IOException network error
     */
    void onRoundStart(String message) throws IOException;

    /**
     * Notify the start of turn state
     *
     * @param message message containing the current round, the turn, the user who has the dice bag and the user
     *                of the current turn
     * @throws IOException network error
     */
    void onTurnState(String message) throws IOException;

    /**
     * Notify the end of the round
     *
     * @param message message containing the current round and the user who has the dice bag
     * @throws IOException network error
     */
    void onRoundEnd(String message) throws IOException;

    /**
     * Notify the end of the game
     *
     * @param roundUser the user of the current round who has the diceBag
     * @throws IOException network error
     */
    void onEndGame(String roundUser) throws IOException;

    /**
     * Notify the skip of a turn
     *
     * @param message     message containing the current round, the turn (true if first), the user who has the dicebag
     *                    and the user of the current turn
     * @throws IOException network error
     */
    void onSkipTurnState(String message) throws IOException;

    /**
     * Notify that the turnUser has decided to place a dice
     *
     * @param turnUser the user who has decided to place a dice
     * @throws IOException network error
     */
    void onPlaceDiceState(String turnUser) throws IOException;

    /**
     * Notify that the turnUser has decided to use a toolCard
     *
     * @param turnUser the user who has decided to use a toolCard
     * @throws IOException network error
     */
    void onUseCardState(String turnUser) throws IOException;

    /**
     * Notify that the turnUser has decided to end his turn
     *
     * @param turnUser the user who has decided to end his turn
     * @throws IOException network error
     */
    void onEndTurnState(String turnUser) throws IOException;

    /**
     * Notify the victory points to all the players
     *
     * @param victoryPoints a map of all victory points (key: username)
     * @throws IOException network error
     */
    void onVictoryPointsCalculated(String victoryPoints) throws IOException;

    /**
     * Notify the winner to the observer
     *
     * @param winner the player who has won the game
     * @throws IOException network error
     */
    void onResultGame(String winner) throws IOException;

    /**
     * Notify that a game has terminated before starting because some player failed to join the game
     */
    void onGameTerminationBeforeStarting() throws IOException;
}
