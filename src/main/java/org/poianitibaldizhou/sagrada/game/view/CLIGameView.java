package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CLIGameView extends UnicastRemoteObject implements IGameView {

    private final transient ConnectionManager connectionManager;

    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;

    private transient UserWrapper currentUser;

    public CLIGameView(ConnectionManager connectionManager) throws RemoteException {
        this.connectionManager = connectionManager;
        this.currentUser = null;
        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();
    }

    public ClientGetMessage getClientGetMessage() {
        return clientGetMessage;
    }

    public ClientCreateMessage getClientCreateMessage() {
        return clientCreateMessage;
    }

    public UserWrapper getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserWrapper currentUser) {
        this.currentUser = currentUser;
    }

    private int readNumber(int maxInt) {
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        consoleListener.stopCommandConsole();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int key = 0;
        do {
            try {
                String read = r.readLine();
                key = Integer.parseInt(read);
                if (!(key > 0 && key <= maxInt))
                    throw new CommandNotFoundException();
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.ERROR_READING, Level.ERROR);
                break;
            } catch (NumberFormatException e) {
                PrinterManager.consolePrint(BuildGraphic.NOT_A_NUMBER, Level.ERROR);
            } catch (CommandNotFoundException e) {
                PrinterManager.consolePrint(BuildGraphic.COMMAND_NOT_FOUND, Level.ERROR);
            }
        } while (key < 1);
        consoleListener.wakeUpCommandConsole();
        return key - 1;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }

    @Override
    public void onPlayersCreate(String players) {
        //TODO
    }

    @Override
    public void onPublicObjectiveCardsDraw(String publicObjectiveCards) {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                buildMessage("Public objective cards valid for this game: ").
                buildGraphicPublicObjectiveCards(publicObjectiveCards).toString(),
                Level.STANDARD);
    }

    @Override
    public void onToolCardsDraw(String toolCards) {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Tool cards valid for this game: ").
                        buildGraphicToolCards(toolCards).toString(),
                Level.STANDARD);
    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Private objective cards valid for this game: ").
                        buildGraphicPrivateObjectiveCards(privateObjectiveCards).toString(),
                Level.STANDARD);
    }

    @Override
    public void onPrivateObjectiveCardDraw(String privateObjectiveCards) {
        //TODO
    }

    @Override
    public void onSchemaCardsDraw(String schemaCards) {
        Runnable reader = () -> {

        };
        new Thread(reader).start();
    }

    /**
     * {{@inheritDoc}}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint("INFORMATION: " + ack + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint("ERROR: " + err + "\n", Level.INFORMATION);
    }
}
