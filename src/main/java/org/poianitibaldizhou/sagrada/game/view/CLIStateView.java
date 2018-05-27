package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BufferManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.observers.IStateObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.HashMap;
import java.util.Map;

public class CLIStateView implements IStateObserver {
    private final CLIGameView cliGameView;
    private final BufferManager bufferManager;

    CLIStateView(CLIGameView cliGameView) {
        this.bufferManager = cliGameView.bufferManager;
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame(){
        bufferManager.consolePrint("Game setup...\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer(){
        bufferManager.consolePrint("Players setup...\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser){
        bufferManager.consolePrint("Start of the round " + (round + 1) + " of the player " +
                roundUser.getName() + "\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser){
        bufferManager.consolePrint("Start of the turn of round " + (round + 1) +
                " and of the player " + turnUser.getName() + "\n", Level.LOW);
        cliGameView.setCurrentUser(roundUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser){
        bufferManager.consolePrint("End of the round " + (round + 1) + " of the player " +
                roundUser.getName() + "\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser){
        bufferManager.consolePrint("The game is ended\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser){
        bufferManager.consolePrint("the turn of round " + (round + 1) +
                " and of the player " + turnUser.getName() + " has been skipped\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser){
        bufferManager.consolePrint("The player  " + turnUser.getName() +
                " has decided to place a dice on his Window Pattern\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser){
        bufferManager.consolePrint("The player " + turnUser.getName() +
                " has decided to use a Tool Card\n", Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser){
        bufferManager.consolePrint("The player " + turnUser.getName() +
                " ends his turn\n", Level.LOW);
    }

    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints){
        bufferManager.consolePrint("Table of the points\n", Level.LOW);
        Map<String, String> points = new HashMap<>();
        victoryPoints.forEach((key, value) -> points.put(key.getUser().getName(), String.valueOf(value)));
        bufferManager.consolePrint(new BuildGraphic().buildGraphicTable(points).toString(), Level.LOW);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner){
        bufferManager.consolePrint("The winner is " + winner.getName(), Level.LOW);
    }
}

