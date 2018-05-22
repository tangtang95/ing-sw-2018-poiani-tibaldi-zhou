package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.io.IOException;
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
        String answer;
        int number;

        do {
            try {
                answer = bufferManager.getConsole().readLine();
            } catch (IOException e) {
                return null;
            }
            if (answer.equals("help"))
                help(commandMap);
            else {
                try {
                    number = Integer.parseInt(answer);
                } catch (NumberFormatException e) {
                    number = 0;
                }
                if (number > 0 && number <= commandMap.keySet().size())
                    return commandMap.get(commandMap.keySet().toArray()[number - 1].toString());
                else
                    bufferManager.formatPrint("WARNING: Command not found", Level.LOW);
            }
        } while (true);
    }

    protected String getAnswer(String question) {
        String answer;

        bufferManager.formatPrint(question, Level.LOW);
        do {
            try {
                answer = bufferManager.getConsole().readLine();
            } catch (IOException e) {
                answer = "help";
            }
            if (answer.equals("help"))
                bufferManager.formatPrint(question, Level.LOW);
            else {
                return answer;
            }
        }while (true);
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
        for (int i = 0; i < commandMap.keySet().size() ; i++) {
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
        bufferManager.formatPrint(stringBuilder.toString(), Level.LOW);
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
    public void run(){
        //...//
    }
}
