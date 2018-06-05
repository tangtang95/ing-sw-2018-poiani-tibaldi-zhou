package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CLIStateScreen extends CLIBasicScreen implements IStateObserver {

    private final transient UserWrapper myUser;
    private final String gameName;
    private final String token;

    private final transient ClientGetMessage clientGetMessage = new ClientGetMessage();
    private final transient ClientCreateMessage clientCreateMessage = new ClientCreateMessage();

    public CLIStateScreen(ConnectionManager connectionManager, ScreenManager screenManager,
                          String gameName, UserWrapper myUser, String token
    ) throws RemoteException {
        super(connectionManager, screenManager);

        this.token = token;
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
    public void onSetupGame() {
        PrinterManager.consolePrint("Game setup...\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        PrinterManager.consolePrint("Players setup...\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundStart(String jString) throws IOException {
        int round = clientGetMessage.getValue(jString);
        UserWrapper roundUser = clientGetMessage.getTurnUserWrapper(jString);
        PrinterManager.consolePrint("The round " + round + " is started with player " +
                        roundUser.getUsername() + "\n", Level.STANDARD);
        screenManager.replaceScreen(new CLIRoundScreen(connectionManager, screenManager, gameName, myUser, token));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTurnState(String jString) throws IOException {
        int round = clientGetMessage.getValue(jString);
        UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
        if(turnUser.equals(myUser)){
            PrinterManager.consolePrint("---------------------------IS YOUR TURN--------------------------\n",
                    Level.STANDARD);
            screenManager.pushScreen(new CLITurnScreen(connectionManager,screenManager,gameName,myUser));
        } else
            PrinterManager.consolePrint("Is the round " + round + "," +
                            turnUser.getUsername() + " is playing\n",
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(String jString) throws IOException {
        int round = clientGetMessage.getValue(jString);
        PrinterManager.consolePrint("The round " + round + "end\n", Level.STANDARD);
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
        UserWrapper user = clientGetMessage.getUserWrapper(jString);
        PrinterManager.consolePrint("The player " + user.getUsername() + " place a dice\n",
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(String jString) throws IOException {
        UserWrapper user = clientGetMessage.getUserWrapper(jString);
        PrinterManager.consolePrint("The player " + user.getUsername() + " use a ToolCard\n",
                Level.STANDARD);
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
                    Level.STANDARD);

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
        if (!(o instanceof CLIStateScreen)) return false;
        if (!super.equals(o)) return false;
        CLIStateScreen that = (CLIStateScreen) o;
        return Objects.equals(myUser, that.myUser) &&
                Objects.equals(gameName, that.gameName) &&
                Objects.equals(clientGetMessage, that.clientGetMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), myUser, gameName, clientGetMessage);
    }
}

