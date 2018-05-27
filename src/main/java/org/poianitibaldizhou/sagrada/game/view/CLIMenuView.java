package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.NetworkManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CLIMenuView extends UnicastRemoteObject implements IScreen {
    protected final transient NetworkManager networkManager;
    protected final transient ScreenManager screenManager;
    protected final transient BufferManager bufferManager;

    static final String NUMBER_WARNING = "WARNING: Number is not correct";

    public CLIMenuView(NetworkManager networkManager, ScreenManager screenManager, BufferManager bufferManager)
            throws RemoteException {
        super();
        this.networkManager = networkManager;
        this.screenManager = screenManager;
        this.bufferManager = bufferManager;
    }


    protected Command getCommand(Map<String, Command> commandMap) {
        BuildGraphic buildGraphic = new BuildGraphic();
        String[] answer = new String[1];
        int number;

        do {
            try {
                bufferManager.consoleRead(answer);
            }catch (NullPointerException e) {
                if(answer[0] == null)
                    throw new NullPointerException();
                else
                    answer[0] = "0";
            }
            if (answer[0].equals("help"))
                bufferManager.consolePrint(buildGraphic.buildGraphicHelp(commandMap).toString(), Level.LOW);
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
