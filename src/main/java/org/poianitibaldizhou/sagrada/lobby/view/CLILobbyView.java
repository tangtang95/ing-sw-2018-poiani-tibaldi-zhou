package org.poianitibaldizhou.sagrada.lobby.view;

import org.poianitibaldizhou.sagrada.lobby.controller.ILobbyServerController;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class CLILobbyView extends UnicastRemoteObject implements ILobbyView, ILobbyObserver {
    private final transient ILobbyServerController controller;
    private final transient Scanner in;
    private String token;
    private Lobby lobby;

    public static final String JOIN_COMMAND = "join";
    public static final String LEAVE_COMMAND = "leave";
    public static final String QUIT_COMMAND = "quit";

    public CLILobbyView(ILobbyServerController controller) throws RemoteException {
        super();
        this.controller = controller;
        in = new Scanner(System.in);
    }

    private void printHelp() {
        System.out.println("===> Available commands:");
        System.out.println("\t" + JOIN_COMMAND + "\t\tjoin the lobby");
        System.out.println("\t" + LEAVE_COMMAND+ "\t\tleave the lobby");
        System.out.println("\t" + QUIT_COMMAND+ "\t\tquit the game");

    }

    private String nextCommand() {
        String command;
        boolean flag = false;
        do {
            System.out.print("===> Next command: ");
            command = in.nextLine();
            if (!command.equals(JOIN_COMMAND) && !command.equals(LEAVE_COMMAND) && !command.equals(QUIT_COMMAND)) {
                printHelp();
            } else {
                flag = true;
            }
        } while (!flag);

        return command;
    }

    public void run() throws RemoteException {
        System.out.println("WELCOME TO SAGRADA!");
        String username;
        do {
            System.out.print("===> Provide an username: ");
            username = in.nextLine();
            if(!(username.isEmpty()))
                token = controller.login(username, this);
        } while(username.isEmpty());

        String command;
        do {
            command = nextCommand();
            if(!command.startsWith(QUIT_COMMAND)) {
                try {
                    switch (command) {
                        case LEAVE_COMMAND:
                            controller.leave(token, username);
                            break;
                        case JOIN_COMMAND:
                            lobby = controller.join(token, username, this);
                            break;
                        default:
                            printHelp();
                    }
                } catch (RemoteException re) {
                    System.out.println("ERROR: " + re.getCause().getMessage());
                }
            }
        }while(!command.startsWith(QUIT_COMMAND));
        controller.logout(token);
        System.out.println("ADIOS");
    }

    @Override
    public void ack(String ack) throws RemoteException {
        System.out.println("===> " + ack);
    }

    @Override
    public void onUserJoin(User user) throws RemoteException{
        System.out.printf("===> User %s joined the lobby", user.getName());
    }

    @Override
    public void onUserExit(User user) throws RemoteException {
        System.out.printf("===> User %s left the lobby", user.getName());
    }

    @Override
    public void onGameStart() throws RemoteException {
        System.out.printf("===> Game started");
    }
}
