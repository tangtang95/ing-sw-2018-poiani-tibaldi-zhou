package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.IStateObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implement the IStateObserver and it takes care
 * of printing the notify of the state on-screen.
 */
public class CLIStateView extends UnicastRemoteObject implements IStateObserver {

    /**
     * Strategy for round.
     */
    private final transient GameModeStrategy roundStrategy;

    /**
     * Strategy for turn.
     */
    private final transient GameModeStrategy turnStrategy;

    /**
     * Reference to my user.
     */
    private final transient UserWrapper myUser;

    /**
     * Reference to the current user.
     */
    private transient UserWrapper currentUser;

    /**
     * Reference at the game name of the current game.
     */
    private final String gameName;

    /**
     * Reference at the player's token.
     */
    private final String token;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage = new ClientGetMessage();

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    private final transient ClientCreateMessage clientCreateMessage = new ClientCreateMessage();

    /**
     * Network manager for connecting with the server.
     */
    private final transient ConnectionManager connectionManager;

    /**
     * Manager for handler the changed of the screen.
     */
    private final transient ScreenManager screenManager;

    /**
     * lock object for synchronizing with the turn start.
     */
    private static final transient Object lock = new Object();

    /**
     * Boolean value use to control the wait status.
     */
    private static boolean start;

    /**
     * Constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameModeStrategy game mode strategy.
     * @param myUser my user in game.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIStateView(ConnectionManager connectionManager, ScreenManager screenManager,
                        GameModeStrategy gameModeStrategy, UserWrapper myUser
                        ) throws RemoteException {
        super();

        this.token = gameModeStrategy.getToken();
        this.myUser = myUser;
        this.gameName = gameModeStrategy.getGameName();
        this.connectionManager = connectionManager;
        this.screenManager = screenManager;
        CLIStateView.setStart(true);

        this.roundStrategy = gameModeStrategy;
        if (gameModeStrategy.isSinglePlayer()){
            this.turnStrategy = gameModeStrategy;
        }else {
            this.turnStrategy = new CLITurnMultiPlayerScreen(connectionManager,screenManager, gameName, token);
        }

    }

    /**
     * @return a reference to connectionManager.
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * @return a reference to the current user.
     */
    public UserWrapper getCurrentUser() {
        if(currentUser == null) {
            try {
                currentUser = clientGetMessage.getUserWrapper(connectionManager.getGameController().getCurrentPlayer(clientCreateMessage.createGameNameMessage(gameName).
                        createTokenMessage(token).buildMessage()));
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.NETWORK_ERROR,
                        Level.ERROR);
            }

        }
        return currentUser;
    }

    /**
     * @return a reference to clientGetMessage.
     */
    public ClientGetMessage getClientGetMessage() {
        return clientGetMessage;
    }

    /**
     * @return a reference to myUser.
     */
    public UserWrapper getMyUser() {
        return myUser;
    }

    /**
     * @return a reference to teh game name.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @return a reference to my token.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return a reference to clientCreateMessage.
     */
    public ClientCreateMessage getClientCreateMessage() {
        return clientCreateMessage;
    }

    /**
     * @return the lock object.
     */
    public static Object getLock() {
        return lock;
    }

    /**
     * Set the start value for the CLITurnScreen
     *
     * @param bool value to set.
     */
    public static void setStart(boolean bool) {
        start = bool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupGame() {
        PrinterManager.consolePrint(ClientMessage.GAME_SETUP, Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetupPlayer() {
        PrinterManager.consolePrint(ClientMessage.PLAYER_SETUP, Level.INFORMATION);
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
            CLIBasicScreen.clearScreen();
            PrinterManager.consolePrint(String.format(ClientMessage.ROUND_STARTED_WITH_PLAYER,(round + 1),
                    roundUser.getUsername()), Level.INFORMATION);
            if (round == 0)
                screenManager.replaceScreen(roundStrategy);
            else {
                setStart(false);
                lock.notifyAll();
            }
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
                CLIBasicScreen.clearScreen();
                PrinterManager.consolePrint(ClientMessage.TURN_MENU,
                        Level.STANDARD);
                screenManager.pushScreen(turnStrategy);

            } else
                PrinterManager.consolePrint(String.format(ClientMessage.USER_PLAYING,(round + 1),
                                turnUser.getUsername()),
                        Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoundEnd(String jString) throws IOException {
        int round = clientGetMessage.getValue(jString);
        PrinterManager.consolePrint(String.format(ClientMessage.ROUND_END, (round + 1)), Level.INFORMATION);
        setStart(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndGame(String roundUser) {
        setStart(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSkipTurnState(String jString) throws IOException {
        UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
        PrinterManager.consolePrint(String.format(ClientMessage.SKIP_TURN,turnUser.getUsername()), Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlaceDiceState(String jString) {
        /*NOT IMPORTANT FOR THE CLI*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUseCardState(String jString) throws IOException {
        UserWrapper user = clientGetMessage.getTurnUserWrapper(jString);
        if (!user.equals(myUser))
            PrinterManager.consolePrint(String.format(ClientMessage.PLAYER_USE_TOOL_CARD,user.getUsername()),
                Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEndTurnState(String jString) throws IOException {
        synchronized (lock) {
            UserWrapper turnUser = clientGetMessage.getTurnUserWrapper(jString);
            if (turnUser.equals(myUser)) {
                CLIBasicScreen.clearScreen();
                PrinterManager.consolePrint(ClientMessage.END_TURN_MENU,
                        Level.STANDARD);
                screenManager.popScreen();
                setStart(true);
            } else
                PrinterManager.consolePrint(String.format(ClientMessage.TURN_END,turnUser.getUsername()),
                        Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onVictoryPointsCalculated(String victoryPoints) throws IOException {
        synchronized (lock) {
            while (start) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            CLIBasicScreen.clearScreen();
            PrinterManager.consolePrint(ClientMessage.TABLE_OF_POINTS_MENU,
                    Level.STANDARD);
            Map<UserWrapper, Integer> points = clientGetMessage.getVictoryPoint(victoryPoints);
            Map<String, String> table = new HashMap<>();
            points.forEach((key, value) -> table.put(key.getUsername(), String.valueOf(value)));
            PrinterManager.consolePrint(new BuildGraphic().buildGraphicTable(table).toString(), Level.STANDARD);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResultGame(String winner) throws IOException {
        synchronized (lock) {
            UserWrapper user = clientGetMessage.getUserWrapper(winner);
            PrinterManager.consolePrint(String.format(ClientMessage.WINNER_IS,user.getUsername()), Level.STANDARD);

            try {
                if(screenManager.getNumberOfScreen() > 2)
                    screenManager.popWithoutStartInScreen();
                screenManager.replaceScreen(new CLIEndGame(connectionManager, screenManager));
            } catch (RemoteException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.NETWORK_ERROR,
                        Level.ERROR);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameTerminationBeforeStarting() {
        PrinterManager.consolePrint(ClientMessage.GAME_TERMINATION_ERROR, Level.ERROR);
        screenManager.popScreen();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStateView is the same or myUser is the same and the game name is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIStateView)) return false;
        if (!super.equals(o)) return false;
        CLIStateView that = (CLIStateView) o;
        return Objects.equals(myUser, that.myUser) &&
                Objects.equals(gameName, that.gameName);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

}

