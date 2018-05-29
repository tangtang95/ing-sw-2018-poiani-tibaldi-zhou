package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.view.CLIBasicView;
import org.poianitibaldizhou.sagrada.game.view.CLIGameView;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class CLILobbyView extends CLIBasicView implements ILobbyView, ILobbyObserver {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private String token;
    private String username;
    private boolean isLoggedIn;
    private final transient ConsoleListener consoleListener;

    private final transient ILobbyController controller;
    private static final String LEAVE_COMMAND = "Leave";
    private static final String TIMEOUT_COMMAND = "Timeout";
    private static final String LOBBY_USER_COMMAND = "Show lobby users";

    public CLILobbyView(ConnectionManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);

        this.controller = networkManager.getLobbyController();
        this.isLoggedIn = false;
        this.username = null;
        this.token = null;

        initializeCommands();
        consoleListener = ConsoleListener.getInstance();
    }

    private void initializeCommands() {
        Command leaveCommand = new Command(LEAVE_COMMAND, "Leave the lobby");
        leaveCommand.setCommandAction(this::leave);
        commandMap.put(leaveCommand.getCommandText(), leaveCommand);

        Command timeoutCommand = new Command(TIMEOUT_COMMAND, "Show time to reach timeout");
        timeoutCommand.setCommandAction(() -> {
            try {
                networkManager.getLobbyController().requestTimeout(token);
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }
        });
        commandMap.put(timeoutCommand.getCommandText(), timeoutCommand);

        Command showUserCommand = new Command(LOBBY_USER_COMMAND, "Show users in lobby");
        showUserCommand.setCommandAction(() -> {
            try {
                networkManager.getLobbyController().requestUsersInLobby(token);
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
        });
        commandMap.put(showUserCommand.getCommandText(), showUserCommand);
    }

    private void leave() {
        isLoggedIn = false;
        try {
            controller.leave(token, username);
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ": Network error.\n", Level.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                        ERROR_READING, Level.ERROR);
                break;
            } catch (IllegalArgumentException e) {
                username = null;
            }
        }

        isLoggedIn = true;

        try {
            controller.join(token, username, this);
        } catch (RemoteException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ": Network error.\n", Level.ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        consoleListener.wakeUpCommandConsole();
    }

    @Override
    public void run() {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint("-----------------------Welcome to the Lobby------------------------",
                Level.STANDARD);
        login();
        PrinterManager.consolePrint(buildGraphic.buildGraphicHelp(commandMap).toString(),Level.STANDARD);
        consoleListener.setCommandMap(commandMap);
        while (isLoggedIn) {
            try {
                sendCommand(commandMap).executeCommand();
            } catch (IOException e) {
                Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
            }catch (NullPointerException e) {
                isLoggedIn = false;
            }
        }
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
    public void onGameStart(String gameName) throws RemoteException {
        PrinterManager.consolePrint("GAME STARTED\n", Level.STANDARD);
        screenManager.replaceScreen(new CLIGameView(networkManager, screenManager,
                gameName, new User(username,token)));

    }

    @Override
    public void onPing(){
        //...
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLILobbyView)) return false;
        if (!super.equals(o)) return false;
        CLILobbyView that = (CLILobbyView) o;
        return isLoggedIn == that.isLoggedIn &&
                Objects.equals(commandMap, that.commandMap) &&
                Objects.equals(controller, that.controller) &&
                Objects.equals(token, that.token) &&
                Objects.equals(username, that.username);
    }
}