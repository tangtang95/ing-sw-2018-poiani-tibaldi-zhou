package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class CLIStateScreen extends CLIBasicScreen implements IStateObserver {

    private final User myUser;
    private final String gameName;

    CLIStateScreen(ConnectionManager connectionManager, ScreenManager screenManager, String gameName, User myUser
                 ) throws RemoteException {
        super(connectionManager,screenManager);

        this.myUser = myUser;
        this.gameName = gameName;
    }

    @Override
    protected void initializeCommands() {
        /*
        For this CLI there are not any commands.
         */
    }

    @Override
    public void startCLI() {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.setCommandMap(commandMap);
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
    public void onRoundStart(String jString){
        String round;
        String roundUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(String jString){
        String round;
        String isFirstTurn;
        String  roundUser;
        String turnUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(String jString){
        String round;
        String roundUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(String roundUser){
        PrinterManager.consolePrint("The game is ended\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(String jString){
        String round;
        String isFirstTurn;
        String roundUser;
        String turnUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(String turnUser){

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(String turnUser){

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(String turnUser){

    }

    @Override
    public void onVictoryPointsCalculated(String victoryPoints){
        PrinterManager.consolePrint("Table of the points\n", Level.STANDARD);
        Map<String, String> points = new HashMap<>();
        //victoryPoints.forEach((key, value) -> points.put(key.getUser().getName(), String.valueOf(value)));
        PrinterManager.consolePrint(new BuildGraphic().buildGraphicTable(points).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(String winner){
        //PrinterManager.consolePrint("The winner is " + winner.getName(), Level.STANDARD);
    }

}

