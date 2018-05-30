package org.poianitibaldizhou.sagrada.game.model.observers.fakeobserversinterfaces;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.rmi.Remote;
import java.util.Map;

public interface IStateFakeObserver {

    /**
     * Notify the start of setup game state
     */
    void onSetupGame() ;

    /**
     * Notify the start of setup player state
     */
    void onSetupPlayer() ;

    /**
     * Notify the start of round
     *
     * @param round     the current round
     * @param roundUser the user of the current round who has the diceBag
     */
    void onRoundStart(int round, User roundUser) ;

    /**
     * Notify the start of turn state
     *
     * @param round       the current round of the turn state
     * @param isFirstTurn indicates if the turn is the first or the second
     * @param roundUser   the user of the current round who has the diceBag
     * @param turnUser    the user of the current turn
     */
    void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) ;

    /**
     * Notify the end of the round
     *
     * @param round     the current round
     * @param roundUser the user of the current round who has the diceBag
     */
    void onRoundEnd(int round, User roundUser) ;

    /**
     * Notify the end of the game
     *
     * @param roundUser the user of the current round who has the diceBag
     */
    void onEndGame(User roundUser) ;

    /**
     * Notify the skip of a turn
     *
     * @param round       the current round
     * @param isFirstTurn if the turn is the first true, otherwise false
     * @param roundUser   the user of the current round who has the diceBag
     * @param turnUser    the user of the current turn
     */
    void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser) ;

    /**
     * Notify that the turnUser has decided to place a dice
     *
     * @param turnUser the user who has decided to place a dice
     */
    void onPlaceDiceState(User turnUser) ;

    /**
     * Notify that the turnUser has decided to use a toolCard
     *
     * @param turnUser the user who has decided to use a toolCard
     */
    void onUseCardState(User turnUser) ;

    /**
     * Notify that the turnUser has decided to end his turn
     *
     * @param turnUser the user who has decided to end his turn
     */
    void onEndTurnState(User turnUser) ;

    /**
     * Notify the victory points to all the players
     *
     * @param victoryPoints a map of all victory points (key: username)
     */
    void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints) ;

    /**
     * Notify the winner to the observer
     *
     * @param winner the player who has won the game
     */
    void onResultGame(User winner) ;
}
