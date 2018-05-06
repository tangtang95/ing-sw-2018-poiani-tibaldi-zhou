package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.cli.IScreen;
import org.poianitibaldizhou.sagrada.cli.ScreenManager;
import org.poianitibaldizhou.sagrada.game.view.CLIGameView;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class CLILobbyView extends UnicastRemoteObject implements ILobbyView, ILobbyObserver, IScreen {
    private final transient BufferedReader reader;
    private final transient Map<String, Command> commandMap;
    private final transient ScreenManager screenManager;
    private final transient NetworkManager networkManager;
    private final transient ILobbyController controller;

    private String token;
    private String username;
    private boolean isLoggedIn;

    private static final String JOIN_COMMAND = "join";
    private static final String LEAVE_COMMAND = "leave";
    private static final String QUIT_COMMAND = "quit";

    public CLILobbyView(NetworkManager networkManager, ScreenManager screenManager) throws RemoteException {
        super();
        this.networkManager = networkManager;
        this.screenManager = screenManager;
        this.controller = networkManager.getLobbyController();
        reader = new BufferedReader(new InputStreamReader(System.in));
        commandMap = new HashMap<>();
        initializeCommands();
    }

    private void initializeCommands() {
        Command joinCommand = new Command(JOIN_COMMAND, "join the lobby");
        joinCommand.setCommandAction(() -> controller.join(token, username, this));
        commandMap.put(joinCommand.getCommandText(), joinCommand);

        Command leaveCommand = new Command(LEAVE_COMMAND, "leave the lobby");
        leaveCommand.setCommandAction(() -> controller.leave(token, username));
        commandMap.put(leaveCommand.getCommandText(), leaveCommand);

        Command quitCommand = new Command(QUIT_COMMAND, "quit the game");
        quitCommand.setCommandAction(() -> isLoggedIn = false);
        commandMap.put(quitCommand.getCommandText(), quitCommand);
    }

    private void printHelp() {
        System.out.println("===> Available commands:");
        for (Command command : commandMap.values()) {
            System.out.println("\t " + command.getCommandText() + "\t\t" + command.getHelpText());
        }
    }

    private Command nextCommand() throws InterruptedException {
        Command command;
        do {
            System.out.print("===> Next command: ");
            String commandText = null;
            try {
                while (!reader.ready())
                    Thread.sleep(10);
                commandText = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            command = commandMap.get(commandText);
            if (command == null)
                printHelp();
        } while (command == null);
        return command;
    }

    @Override
    public void run() throws InterruptedException{
        System.out.println("WELCOME TO SAGRADA!");
        do {
            System.out.print("===> Provide an username: ");
            try {
                username = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!(username.isEmpty()))
                try {
                    token = controller.login(username, this);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        } while (username.isEmpty() || token.isEmpty());
        isLoggedIn = true;

        Command command;
        do {
            try {
                command = nextCommand();
            } catch (InterruptedException e) {
                throw new InterruptedException("interrupt caught");
            }
            try {
                command.executeCommand();
            } catch (RemoteException re) {
                System.out.println("ERROR: " + re.getCause().getMessage());
            }
        } while (isLoggedIn);
        try {
            controller.logout(token);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("ADIOS");
    }

    @Override
    public void ack(String ack) throws RemoteException {
        System.out.println("===> " + ack);
    }

    @Override
    public void err(String err) throws RemoteException {
        System.out.println("===> Error: " + err);
    }

    public void onUserJoin(User user) throws RemoteException {
        System.out.printf("===> User %s joined the lobby", user.getName());
    }

    @Override
    public void onUserExit(User user) throws RemoteException {
        System.out.printf("===> User %s left the lobby", user.getName());
    }

    @Override
    public void onGameStart() throws RemoteException {
        System.out.print("===> Game started");
        screenManager.pushScreen(new CLIGameView(networkManager, screenManager));
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}