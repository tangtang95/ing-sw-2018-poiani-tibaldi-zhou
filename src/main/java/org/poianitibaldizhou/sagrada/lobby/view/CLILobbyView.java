package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIGameView;
import org.poianitibaldizhou.sagrada.game.view.CLIMenuView;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class CLILobbyView extends CLIMenuView implements ILobbyView, ILobbyObserver {
    private final transient Map<String, Command> commandMap = new HashMap<>();

    private String token;
    private String username;
    private boolean isLoggedIn;

    private final transient ILobbyController controller;
    private static final String LEAVE_COMMAND = "Leave";
    private static final String TIMEOUT_COMMAND = "Timeout";
    private static final String LOBBY_USER_COMMAND = "Show lobby users";

    public CLILobbyView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super(networkManager, screenManager);
        this.controller = networkManager.getLobbyController();
        this.isLoggedIn = false;

        initializeCommands();
    }

    private void initializeCommands() {
        Command leaveCommand = new Command(LEAVE_COMMAND, "Leave the lobby");
        leaveCommand.setCommandAction(this::leave);
        commandMap.put(leaveCommand.getCommandText(), leaveCommand);

        Command timeoutCommand = new Command(TIMEOUT_COMMAND, "Show time to reach timeout");
        timeoutCommand.setCommandAction(() -> networkManager.getLobbyController().requestTimeout(token));
        commandMap.put(timeoutCommand.getCommandText(), timeoutCommand);

        Command showUserCommand = new Command(LOBBY_USER_COMMAND, "Show users in lobby");
        showUserCommand.setCommandAction(() -> networkManager.getLobbyController().requestUsersInLobby(token));
        commandMap.put(showUserCommand.getCommandText(), showUserCommand);
    }

    private void leave() {
        this.isLoggedIn = false;
        try {
            controller.leave(token, username);
            controller.logout(token);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }
        screenManager.popScreen();
    }

    private void login() {
        do {
            username = getAnswer("Provide an username: ");
            if (!(username.isEmpty()))
                try {
                    token = controller.login(username, this);
                } catch (RemoteException e) {
                    Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
                }
        } while (username.isEmpty() || token.isEmpty());

        isLoggedIn = true;
        try {
            controller.join(token, username, this);
        } catch (RemoteException e) {
            Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    @Override
    public void run() {
        bufferManager.formatPrint("-----------------------Welcome to the Lobby------------------------",
                Level.LOW);
        login();

        help(commandMap);
        while (isLoggedIn) {
            try {
                getCommand(commandMap).executeCommand();
            } catch (RemoteException e) {
                Logger.getAnonymousLogger().log(java.util.logging.Level.SEVERE, e.toString());
            }
        }
    }

    @Override
    public void ack(String ack) {
        bufferManager.formatPrint("ACK: " + ack, Level.HIGH);
    }

    @Override
    public void err(String err) {
        bufferManager.formatPrint("ERROR: " + err, Level.HIGH);
    }

    public void onUserJoin(User user) {
        if (!user.getName().equals(username))
            bufferManager.formatPrint("User " + user.getName() + " joined the Lobby", Level.HIGH);
    }

    @Override
    public void onUserExit(User user) {
        if (!user.getName().equals(username))
            bufferManager.formatPrint("User " + user.getName() + " left the Lobby", Level.HIGH);
    }

    @Override
    public void onGameStart() throws RemoteException {
        bufferManager.formatPrint("GAME STARTED", Level.HIGH);
        screenManager.replaceScreen(new CLIGameView(networkManager, screenManager));
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