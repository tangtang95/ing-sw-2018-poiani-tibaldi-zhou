package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.view.CLIBasicScreen;
import org.poianitibaldizhou.sagrada.game.view.CLIMultiPlayerScreen;
import org.poianitibaldizhou.sagrada.game.view.CLISetupGameScreen;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

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

    private final transient ClientGetMessage clientGetMessage;
    private transient ClientCreateMessage clientCreateMessage;

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
        this.clientGetMessage = new ClientGetMessage();
        this.clientCreateMessage = new ClientCreateMessage();

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
                String message = connectionManager.getLobbyController().getTimeout();
                String timeout = clientGetMessage.getTimeout(message);
                PrinterManager.consolePrint(timeout + "\n",Level.STANDARD);
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
            }
        });
        commandMap.put(timeoutCommand.getCommandText(), timeoutCommand);

        Command showUserCommand = new Command(LOBBY_USER_COMMAND, "Show users in lobby");
        showUserCommand.setCommandAction(() -> {
            try {
                String message = connectionManager.getLobbyController().getUsersInLobby();
                PrinterManager.consolePrint(clientGetMessage.getListOfUserWrapper(message).toString() + "\n", Level.STANDARD);
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
            controller.leave(clientCreateMessage.createTokenMessage(token).createUsernameMessage(username).buildMessage());
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.NETWORK_ERROR , Level.ERROR);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.ERROR_READING , Level.ERROR);
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
                    String message = controller.login(clientCreateMessage.createUsernameMessage(username).buildMessage(),
                            this);
                    token = clientGetMessage.getToken(message);
                    if (token.isEmpty())
                        throw new IllegalArgumentException();
                }
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
            } catch (IllegalArgumentException e) {
                username = null;
            }
        }

        try {
            controller.join(clientCreateMessage.createUsernameMessage(username).createTokenMessage(token).buildMessage(), this);
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
        CLIBasicScreen.clearScreen();
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint("-----------------------Welcome to the Lobby------------------------\n",
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
    public void ping() {
        // DO-NOTHING
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserJoin(String user) throws IOException {
        UserWrapper userWrapper = clientGetMessage.getUserWrapper(user);
        if (!userWrapper.getUsername().equals(username))
            PrinterManager.consolePrint("User " + userWrapper.getUsername() + " joined the Lobby\n", Level.INFORMATION);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUserExit(String user) throws IOException {
        UserWrapper userWrapper = clientGetMessage.getUserWrapper(user);
        if (!userWrapper.getUsername().equals(username)) {
            PrinterManager.consolePrint("User " + userWrapper.getUsername() + " left the Lobby\n", Level.INFORMATION);
        } else {
            PrinterManager.consolePrint("You have left the lobby.\n", Level.INFORMATION);
            screenManager.popScreen();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onGameStart(String message) throws IOException {
        String gameName = clientGetMessage.getGameName(message);

        PrinterManager.consolePrint("\"----------------------------GAME STARTED---------------------------\n", Level.STANDARD);
        try {
            screenManager.replaceScreen(new CLISetupGameScreen(connectionManager,screenManager,
                    new CLIMultiPlayerScreen(connectionManager,screenManager,gameName,token),
                    new UserWrapper(username)));
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