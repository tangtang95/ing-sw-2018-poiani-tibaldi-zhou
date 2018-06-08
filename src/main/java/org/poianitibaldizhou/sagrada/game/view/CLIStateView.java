package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStateView extends UnicastRemoteObject implements IStateObserver {

    private final transient UserWrapper myUser;
    private transient UserWrapper currentUser;
    private final String gameName;
    private final String token;

    private final transient ClientGetMessage clientGetMessage = new ClientGetMessage();
    private final transient ClientCreateMessage clientCreateMessage = new ClientCreateMessage();
    private final transient ConnectionManager connectionManager;
    private final transient ScreenManager screenManager;

    private final transient Object lock = new Object();
    private boolean start;

    public CLIStateView(ConnectionManager connectionManager, ScreenManager screenManager,
                        String gameName, UserWrapper myUser, String token
    ) throws RemoteException {

        this.token = token;
        this.myUser = myUser;
        this.gameName = gameName;
        this.connectionManager = connectionManager;
        this.screenManager = screenManager;
        this.start = true;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public UserWrapper getCurrentUser() {
        return currentUser;
    }

    public ClientGetMessage getClientGetMessage() {
        return clientGetMessage;
    }

    public UserWrapper getMyUser() {
        return myUser;
    }

    public String getGameName() {
        return gameName;
    }

    public String getToken() {
        return token;
    }

    public ClientCreateMessage getClientCreateMessage() {
        return clientCreateMessage;
    }

    public Object getLock() {
        return lock;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        PrinterManager.consolePrint("Game setup.\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        PrinterManager.consolePrint("Players setup.\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(String jString) throws IOException {
        synchronized (lock) {
            int round = clientGetMessage.getValue(jString);
            UserWrapper roundUser = clientGetMessage.getRoundUser(jString);
            currentUser = roundUser;
            PrinterManager.consolePrint("The round " + (round + 1) + " is started with player " +
                    roundUser.getUsername() + "\n", Level.INFORMATION);
            screenManager.replaceScreen(new CLIRoundScreen(connectionManager, screenManager, this));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(String jString) throws IOException {
        synchronized (lock) {
            while (start) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            int round = clientGetMessage.getValue(jString);
            UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
            currentUser = turnUser;

            if (turnUser.equals(myUser)) {
                PrinterManager.consolePrint("---------------------------IS YOUR TURN--------------------------\n",
                        Level.STANDARD);
                screenManager.pushScreen(new CLITurnScreen(connectionManager, screenManager, this));

            } else
                PrinterManager.consolePrint("Is the round " + (round + 1) + "," +
                                turnUser.getUsername() + " is playing\n",
                        Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(String jString) throws IOException {
        int round = clientGetMessage.getValue(jString);
        PrinterManager.consolePrint("The round " + (round + 1) + "end\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(String roundUser) {
        PrinterManager.consolePrint("The game is ended\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(String jString) throws IOException {
        UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
        PrinterManager.consolePrint("The turn of player " + turnUser.getUsername() +
        "skip\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(String jString) throws IOException {
        UserWrapper user = clientGetMessage.getTurnUserWrapper(jString);
        if (!user.equals(myUser))
            PrinterManager.consolePrint("The player " + user.getUsername() + " place a dice\n",
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(String jString) throws IOException {
        UserWrapper user = clientGetMessage.getTurnUserWrapper(jString);
        if (!user.equals(myUser))
            PrinterManager.consolePrint("The player " + user.getUsername() + " use a ToolCard\n",
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(String jString) throws IOException {
        UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
        if(turnUser.equals(myUser)){
            PrinterManager.consolePrint("-------------------------YOUR TURN IS FINISH------------------------\n",
                    Level.STANDARD);
            screenManager.popScreen();
        } else
            PrinterManager.consolePrint("The turn of " + turnUser.getUsername() + " is ending\n",
                    Level.INFORMATION);

    }

    @Override
    public void onVictoryPointsCalculated(String victoryPoints) throws IOException {
        PrinterManager.consolePrint("Table of the points\n", Level.STANDARD);
        Map<UserWrapper, Integer> points = clientGetMessage.getVictoryPoint(victoryPoints);
        Map<String,String> table = new HashMap<>();
        points.forEach((key, value) -> table.put(key.getUsername(), String.valueOf(value)));
        PrinterManager.consolePrint(new BuildGraphic().buildGraphicTable(table).toString(), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(String winner) throws IOException {
        UserWrapper user = clientGetMessage.getUserWrapper(winner);
        PrinterManager.consolePrint("The winner is " + user.getUsername() + "\n", Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStateView)) return false;
        if (!super.equals(o)) return false;
        CLIStateView that = (CLIStateView) o;
        return Objects.equals(myUser, that.myUser) &&
                Objects.equals(gameName, that.gameName);
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}

