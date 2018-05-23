package org.poianitibaldizhou.sagrada.game.model.observers;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IStateObserver extends Remote {

    /**
     * Notify the start of setup game state
     *
     * @throws RemoteException network error
     */
    void onSetupGame() throws RemoteException;

    /**
     * Notify the start of setup player state
     *
     * @throws RemoteException network error
     */
    void onSetupPlayer() throws RemoteException;

    /**
     * Notify the start of round
     *
     * @param round     the current round
     * @param roundUser the user of the current round who has the diceBag
     * @throws RemoteException network error
     */
    void onRoundStart(int round, User roundUser) throws RemoteException;

    /**
     * Notify the start of turn state
     *
     * @param round       the current round of the turn state
     * @param isFirstTurn indicates if the turn is the first or the second
     * @param roundUser   the user of the current round who has the diceBag
     * @param turnUser    the user of the current turn
     * @throws RemoteException network error
     */
    void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) throws RemoteException;

    /**
     * Notify the end of the round
     *
     * @param round     the current round
     * @param roundUser the user of the current round who has the diceBag
     * @throws RemoteException network error
     */
    void onRoundEnd(int round, User roundUser) throws RemoteException;

    /**
     * Notify the end of the game
     *
     * @param roundUser the user of the current round who has the diceBag
     * @throws RemoteException network error
     */
    void onEndGame(User roundUser) throws RemoteException;

    /**
     * Notify the skip of a turn
     *
     * @param round       the current round
     * @param isFirstTurn if the turn is the first true, otherwise false
     * @param roundUser   the user of the current round who has the diceBag
     * @param turnUser    the user of the current turn
     * @throws RemoteException network error
     */
    void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) throws RemoteException;

    /**
     * Notify that the turnUser has decided to place a dice
     *
     * @param turnUser the user who has decided to place a dice
     * @throws RemoteException network error
     */
    void onPlaceDiceState(User turnUser) throws RemoteException;

    /**
     * Notify that the turnUser has decided to use a toolCard
     *
     * @param turnUser the user who has decided to use a toolCard
     * @throws RemoteException network error
     */
    void onUseCardState(User turnUser) throws RemoteException;

    /**
     * Notify that the turnUser has decided to end his turn
     *
     * @param turnUser the user who has decided to end his turn
     * @throws RemoteException network error
     */
    void onEndTurnState(User turnUser) throws RemoteException;

    /**
     * Notify the victory points to all the players
     *
     * @param victoryPoints a map of all victory points (key: username)
     * @throws RemoteException network error
     */
    void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) throws RemoteException;

    /**
     * Notify the winner to the observer
     *
     * @param winner the player who has won the game
     * @throws RemoteException network error
     */
    void onResultGame(User winner) throws RemoteException;
}
