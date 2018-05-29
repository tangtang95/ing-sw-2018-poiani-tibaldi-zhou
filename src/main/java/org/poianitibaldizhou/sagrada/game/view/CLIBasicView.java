package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class CLIBasicView extends UnicastRemoteObject implements IScreen {
    protected final transient ConnectionManager networkManager;
    protected final transient ScreenManager screenManager;

    public static final String NUMBER_WARNING = "Number is not correct.\n";
    public static final String NOT_A_NUMBER = "Is not a number, please retry.\n";
    public static final String COMMAND_NOT_FOUND = "Command not found, please retry.\n";
    public static final String ERROR_READING = ": Error while reading from keyboard.\n";

    public CLIBasicView(ConnectionManager networkManager, ScreenManager screenManager)
            throws RemoteException {
        super();
        this.networkManager = networkManager;
        this.screenManager = screenManager;
    }


    protected Command sendCommand(Map<String, Command> commandMap) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        BuildGraphic buildGraphic = new BuildGraphic();
        int key = 0;

        do{
            try {
                String read = r.readLine();
                if (read.equals("help"))
                    PrinterManager.consolePrint(buildGraphic.buildGraphicHelp(commandMap).toString(), Level.STANDARD);
                else {
                    key = Integer.parseInt(read);
                    if (!(key > 0 && key <= commandMap.keySet().size()))
                        throw new CommandNotFoundException();
                }
            } catch (NumberFormatException e) {
                key = 0;
                PrinterManager.consolePrint(NOT_A_NUMBER, Level.ERROR);
            } catch (CommandNotFoundException e) {
                key = 0;
                PrinterManager.consolePrint(COMMAND_NOT_FOUND, Level.ERROR);
            }
        }while(key < 1);
        return commandMap.get(commandMap.keySet().toArray()[key - 1].toString());
    }

    protected String getAnswer(String question) {

        PrinterManager.consolePrint(question + "\n", Level.STANDARD);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIBasicView)) return false;
        if (!super.equals(o)) return false;
        CLIBasicView cliBasicView = (CLIBasicView) o;
        return Objects.equals(networkManager, cliBasicView.networkManager) &&
                Objects.equals(screenManager, cliBasicView.screenManager);
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
