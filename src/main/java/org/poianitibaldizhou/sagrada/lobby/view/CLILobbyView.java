package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.ScreenManager;
import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLILobbyView extends UnicastRemoteObject implements ILobbyView, ILobbyObserver, IScreen {
    private final transient ILobbyController controller;
    private final transient Scanner in;
    private final transient Map<String, Command> commandMap;
    private final transient ScreenManager screenManager;

    private String token;
    private String username;
    private boolean isLoggedIn;

    private static final String JOIN_COMMAND = "join";
    private static final String LEAVE_COMMAND = "leave";
    private static final String QUIT_COMMAND = "quit";

    public CLILobbyView(ILobbyController controller, ScreenManager screenManager) throws RemoteException {
        super();
        this.controller = controller;
        this.screenManager = screenManager;
        in = new Scanner(System.in);
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
        for (Command command: commandMap.values()) {
            System.out.println("\t " + command.getCommandText() + "\t\t" + command.getHelpText());
        }
    }

    private Command nextCommand() {
        Command command;
        do {
            System.out.print("===> Next command: ");
            String commandText = in.nextLine();
            command = commandMap.get(commandText);
            if (command == null)
                printHelp();
        } while (command == null);
        return command;
    }

    public void run() {
        System.out.println("WELCOME TO SAGRADA!");
        do {
            System.out.print("===> Provide an username: ");
            username = in.nextLine();
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
            command = nextCommand();
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
    }

    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }
}