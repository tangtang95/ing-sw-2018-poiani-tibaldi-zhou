package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.BuildGraphic;
import org.poianitibaldizhou.sagrada.cli.ConsoleListener;
import org.poianitibaldizhou.sagrada.cli.Level;
import org.poianitibaldizhou.sagrada.cli.PrinterManager;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.SchemaCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.ToolCardWrapper;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class CLIGameView extends UnicastRemoteObject implements IGameView {

    private final transient ConnectionManager connectionManager;

    private final transient CLIStateScreen cliStateScreen;
    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;
    private final transient String token;
    private final transient String gameName;

    public CLIGameView(CLIStateScreen cliStateScreen, ConnectionManager connectionManager) throws RemoteException {
        super();
        this.connectionManager = connectionManager;
        this.gameName = cliStateScreen.getGameName();
        this.token = cliStateScreen.getToken();
        this.clientCreateMessage = cliStateScreen.getClientCreateMessage();
        this.clientGetMessage = cliStateScreen.getClientGetMessage();
        this.cliStateScreen = cliStateScreen;
    }

    @Override
    public void onPlayersCreate(String jString) throws IOException {
        List<UserWrapper> users = clientGetMessage.getListOfUserWrapper(jString);
        users.forEach(user -> {
            try {
                connectionManager.getGameController().bindPlayer(
                        clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage(),
                        new CLIPlayerView(cliStateScreen),
                        new CLISchemaCardView(cliStateScreen)
                );
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.NETWORK_ERROR, Level.ERROR);
            }
        });

    }

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
        List<ToolCardWrapper> toolCards = clientGetMessage.getToolCards(jString);
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Tool cards valid for this game: ").
                        buildGraphicToolCards(toolCards).toString(),
                Level.STANDARD);
        for (ToolCardWrapper t : toolCards)
            connectionManager.getGameController().bindToolCard(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).
                            createToolCardMessage(t).buildMessage(),
                    new CLIToolCardView(cliStateScreen, t.getName())
            );
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
        ConsoleListener consoleListener = ConsoleListener.getInstance();
        List<SchemaCardWrapper> schemaCards = new ArrayList<>();
        clientGetMessage.getFrontBackSchemaCards(jString).forEach(c -> schemaCards.addAll(c.getSchemaCards()));

        buildGraphic.buildMessage("Choose a schema card:");
        for (int i = 0; i < schemaCards.size(); i++)
            buildGraphic.buildMessage("" + i).buildGraphicSchemaCard(schemaCards.get(i));
        PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);

        connectionManager.getGameController().chosenSchemaCard(
                clientCreateMessage.createSchemaCardMessage(
                        schemaCards.get(consoleListener.readNumber(schemaCards.size()))).
                        createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint(ack + "\n", Level.INFORMATION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint(err + "\n", Level.INFORMATION);
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CLIGameView)) return false;
        if (!super.equals(o)) return false;
        CLIGameView that = (CLIGameView) o;
        return Objects.equals(connectionManager, that.connectionManager) &&
                Objects.equals(cliStateScreen, that.cliStateScreen) &&
                Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage) &&
                Objects.equals(token, that.token) &&
                Objects.equals(gameName, that.gameName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), connectionManager, cliStateScreen, clientGetMessage,
                clientCreateMessage, token, gameName);
    }


}
