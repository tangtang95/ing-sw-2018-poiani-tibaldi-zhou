package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CLIMenuView extends UnicastRemoteObject implements IScreen {
    protected final transient NetworkManager networkManager;
    protected final transient ScreenManager screenManager;
    protected final transient BufferManager bufferManager;

    public CLIMenuView(NetworkManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super();
        this.networkManager = networkManager;
        this.screenManager = screenManager;
        this.bufferManager = new BufferManager();
    }


    protected Command getCommand(Map<String, Command> commandMap) {
        String[] answer = new String[1];
        int number;

        do {
            try {
                bufferManager.consoleRead(answer);
            }catch (NullPointerException e) {
                throw new NullPointerException();
            }
            if (answer[0].equals("help"))
                help(commandMap);
            else {
                try {
                    number = Integer.parseInt(answer[0]);
                } catch (NumberFormatException e) {
                    number = 0;
                }
                if (number > 0 && number <= commandMap.keySet().size())
                    return commandMap.get(commandMap.keySet().toArray()[number - 1].toString());
                else {
                    bufferManager.consolePrint("WARNING: Command not found", Level.HIGH);
                }
            }
        } while (true);
    }

    protected String getAnswer(String question) {
        String[] answer = new String[1];

        bufferManager.consolePrint(question, Level.LOW);
        do {
            bufferManager.consoleRead(answer);
            if (answer[0].equals("help")) {
                bufferManager.consolePrint(question, Level.LOW);
            }
            else {
                if (!answer[0].equals(""))
                    return answer[0];
            }
        } while (true);
    }

    protected void help(Map<String, Command> commandMap) {
        int maxLength = 0;
        StringBuilder stringBuilder = new StringBuilder();
        Command command;

        stringBuilder.append("Available commands:");
        stringBuilder.append("\n");
        for (Command com : commandMap.values())
            if (com.getCommandText().length() > maxLength)
                maxLength = com.getCommandText().length();
        for (int i = 0; i < commandMap.keySet().size(); i++) {
            command = commandMap.get(commandMap.keySet().toArray()[i].toString());
            stringBuilder.append("[");
            stringBuilder.append((i + 1));
            stringBuilder.append("] ");
            stringBuilder.append(command.getCommandText());
            for (int j = 0; j < maxLength - command.getCommandText().length(); j++)
                stringBuilder.append(" ");
            stringBuilder.append("\t\t");
            stringBuilder.append(command.getHelpText());
            stringBuilder.append("\n");
        }
        bufferManager.consolePrint(stringBuilder.toString(), Level.LOW);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIMenuView)) return false;
        if (!super.equals(o)) return false;
        CLIMenuView cliMenuView = (CLIMenuView) o;
        return Objects.equals(networkManager, cliMenuView.networkManager) &&
                Objects.equals(screenManager, cliMenuView.screenManager);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), networkManager, screenManager);
    }

    @Override
    public void run() {
        //...//
    }
}
