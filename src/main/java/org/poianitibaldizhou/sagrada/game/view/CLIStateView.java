package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStateView extends UnicastRemoteObject implements IStateObserver {
    private final CLIGameView cliGameView;

    CLIStateView(CLIGameView cliGameView) throws RemoteException {
        super();
        this.cliGameView = cliGameView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame(){
        PrinterManager.consolePrint("Game setup...\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
     public void onSetupPlayer(){
     PrinterManager.consolePrint("Players setup...\n", Level.STANDARD);
     }

     /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(int round, User roundUser){
        PrinterManager.consolePrint("Start of the round " + (round + 1) + " of the player " +
                roundUser.getName() + "\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser){
        PrinterManager.consolePrint("Start of the turn of round " + (round + 1) +
                " and of the player " + turnUser.getName() + "\n", Level.STANDARD);
        cliGameView.setCurrentUser(roundUser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(int round, User roundUser){
        PrinterManager.consolePrint("End of the round " + (round + 1) + " of the player " +
                roundUser.getName() + "\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(User roundUser){
        PrinterManager.consolePrint("The game is ended\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(int round, boolean isFirstTurn, User roundUser, User turnUser){
        PrinterManager.consolePrint("the turn of round " + (round + 1) +
                " and of the player " + turnUser.getName() + " has been skipped\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(User turnUser){
        PrinterManager.consolePrint("The player  " + turnUser.getName() +
                " has decided to place a dice on his Window Pattern\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(User turnUser){
        PrinterManager.consolePrint("The player " + turnUser.getName() +
                " has decided to use a Tool Card\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(User turnUser){
        PrinterManager.consolePrint("The player " + turnUser.getName() +
                " ends his turn\n", Level.STANDARD);
    }

    @Override
    public void onVictoryPointsCalculated(Map<Player, Integer> victoryPoints){
        PrinterManager.consolePrint("Table of the points\n", Level.STANDARD);
        Map<String, String> points = new HashMap<>();
        victoryPoints.forEach((key, value) -> points.put(key.getUser().getName(), String.valueOf(value)));
        PrinterManager.consolePrint(new BuildGraphic().buildGraphicTable(points).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(User winner){
        PrinterManager.consolePrint("The winner is " + winner.getName(), Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStateView)) return false;
        if (!super.equals(o)) return false;
        CLIStateView that = (CLIStateView) o;
        return Objects.equals(cliGameView, that.cliGameView);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cliGameView);
    }
}

