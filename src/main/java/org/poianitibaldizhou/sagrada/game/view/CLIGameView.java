package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.exception.CommandNotFoundException;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CLIGameView extends UnicastRemoteObject implements IGameView {

    private final transient ConnectionManager connectionManager;

    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;
    private final transient String token;
    private final transient String gameName;

    public CLIGameView(ConnectionManager connectionManager, String token, String gameName) throws RemoteException {
        this.connectionManager = connectionManager;
        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();
        this.gameName = gameName;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getGameName() {
        return gameName;
    }

    public ClientGetMessage getClientGetMessage() {
        return clientGetMessage;
    }

    public ClientCreateMessage getClientCreateMessage() {
        return clientCreateMessage;
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
    public void onPublicObjectiveCardsDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                buildMessage("Public objective cards valid for this game: ").
                buildGraphicPublicObjectiveCards(clientGetMessage.getPublicObjectiveCards(jString)).toString(),
                Level.STANDARD);
    }

    @Override
    public void onToolCardsDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Tool cards valid for this game: ").
                        buildGraphicToolCards(clientGetMessage.getToolCards(jString)).toString(),
                Level.STANDARD);
    }

    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) {
        /*SINGLE PLAYER*/
    }

    @Override
    public void onPrivateObjectiveCardDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Private objective cards valid for this game: ").
                        buildGraphicPrivateObjectiveCards(clientGetMessage.getPrivateObjectiveCards(jString)).toString(),
                Level.STANDARD);
    }

    @Override
    public void onSchemaCardsDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<SchemaCardWrapper> schemaCards = new ArrayList<>();
        clientGetMessage.getFrontBackSchemaCards(jString).forEach(c -> schemaCards.addAll(c.getSchemaCards()));

        buildGraphic.buildMessage("Choose a schema card:");
        for (int i = 0; i < schemaCards.size(); i++)
            buildGraphic.buildMessage("" + i).buildGraphicSchemaCard(schemaCards.get(i));
        PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);

        connectionManager.getGameController().chosenSchemaCard(
                clientCreateMessage.createSchemaCardMessage(
                        schemaCards.get(readNumber(schemaCards.size()))).
                        createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
        );
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIGameView)) return false;
        if (!super.equals(o)) return false;
        CLIGameView that = (CLIGameView) o;
        return Objects.equals(getConnectionManager(), that.getConnectionManager()) &&
                Objects.equals(getClientGetMessage(), that.getClientGetMessage()) &&
                Objects.equals(getClientCreateMessage(), that.getClientCreateMessage()) &&
                Objects.equals(getToken(), that.getToken()) &&
                Objects.equals(getGameName(), that.getGameName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getConnectionManager(), getClientGetMessage(), getClientCreateMessage(), getToken(), getGameName());
    }
}
