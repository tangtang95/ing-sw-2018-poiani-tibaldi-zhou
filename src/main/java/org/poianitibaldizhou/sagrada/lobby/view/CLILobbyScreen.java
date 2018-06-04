package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.view.CLIBasicScreen;
import org.poianitibaldizhou.sagrada.game.view.CLIRoundScreen;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * CLI Lobby view.
 */
public class CLILobbyScreen extends CLIBasicScreen implements ILobbyView, ILobbyObserver {

    /**
     * User's token for the login.
     */
    private String token;

    /**
     * User's username for the login.
     */
    private String username;

    /**
     * Instance of the console listener.
     */
    private final transient ConsoleListener consoleListener;

    /**
     * Controller of the lobby.
     */
    private final transient ILobbyController controller;

    /**
     * Lobby commands.
     */
    private static final String LEAVE_COMMAND = "Leave";
    private static final String TIMEOUT_COMMAND = "Timeout";
    private static final String LOBBY_USER_COMMAND = "Show lobby users";

    /**
     * constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLILobbyScreen(ConnectionManager connectionManager, ScreenManager screenManager)
            throws RemoteException {
        super(connectionManager, screenManager);

        this.controller = connectionManager.getLobbyController();
        this.username = null;
        this.token = null;

        initializeCommands();
        consoleListener = ConsoleListener.getInstance();
    }

    /**
     * Initialize the Lobby's commands.
     */
    @Override
    protected void initializeCommands() {
        Command leaveCommand = new Command(LEAVE_COMMAND, "Leave the lobby");
        leaveCommand.setCommandAction(this::leave);
        commandMap.put(leaveCommand.getCommandText(), leaveCommand);

        Command timeoutCommand = new Command(TIMEOUT_COMMAND, "Show time to reach timeout");
        timeoutCommand.setCommandAction(() -> {
            try {
                connectionManager.getLobbyController().getTimeout();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
            }
        });
        commandMap.put(timeoutCommand.getCommandText(), timeoutCommand);

        Command showUserCommand = new Command(LOBBY_USER_COMMAND, "Show users in lobby");
        showUserCommand.setCommandAction(() -> {
            try {
                connectionManager.getLobbyController().getUsersInLobby();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
            }
        });
        commandMap.put(showUserCommand.getCommandText(), showUserCommand);
    }

    /**
     * Leave from the Lobby.
     */
    private void leave() {
        try {
            controller.leave(token, username);
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.NETWORK_ERROR, Level.ERROR);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.ERROR_READING, Level.ERROR);
        }
    }

    /**
     * Login in the Lobby.
     */
    private void login() {
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        PrinterManager.consolePrint("Provide an username: \n", Level.STANDARD);
        while(username == null) {
            try {
                username = r.readLine();
                if (username.equals(""))
                    throw new IllegalArgumentException();
                else {
                    token = controller.login(username, this);
                }
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                username = null;
            }
        }
        try {
            controller.join(token, username, this);
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.ERROR_READING, Level.ERROR);
        }
        consoleListener.wakeUpCommandConsole();
    }

    /**
     * Start the CLI.
     */
    @Override
    public void startCLI() {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint("-----------------------Welcome to the Lobby------------------------",
                Level.STANDARD);
        login();
        PrinterManager.consolePrint(buildGraphic.buildGraphicHelp(commandMap).toString(),Level.STANDARD);
        consoleListener.setCommandMap(commandMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint(ack + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint(err+ "\n", Level.ERROR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserJoin(User user) {
        if (!user.getName().equals(username))
            PrinterManager.consolePrint("User " + user.getName() + " joined the Lobby\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserExit(User user){
        if (!user.getName().equals(username)) {
            PrinterManager.consolePrint("User " + user.getName() + " left the Lobby\n", Level.INFORMATION);
        } else {
            PrinterManager.consolePrint("You have left the lobby.\n", Level.INFORMATION);
            screenManager.popScreen();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameStart(String gameName) {
        PrinterManager.consolePrint("GAME STARTED\n", Level.STANDARD);
        try {
            screenManager.replaceScreen(new CLIRoundScreen(connectionManager,screenManager,
                    gameName,new User(username,token)));
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPing(){
        //...
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLILobbyScreen has the same commandMap, controller, token, username.
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLILobbyScreen)) return false;
        if (!super.equals(o)) return false;
        CLILobbyScreen that = (CLILobbyScreen) o;
        return  Objects.equals(commandMap, that.commandMap) &&
                Objects.equals(controller, that.controller) &&
                Objects.equals(token, that.token) &&
                Objects.equals(username, that.username);
    }
}