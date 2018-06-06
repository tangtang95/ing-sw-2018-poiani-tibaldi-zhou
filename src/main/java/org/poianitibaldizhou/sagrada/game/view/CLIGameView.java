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

    private final transient CLIStateView cliStateView;
    private final transient ClientGetMessage clientGetMessage;
    private final transient ClientCreateMessage clientCreateMessage;
    private final transient String token;
    private final transient String gameName;

    public CLIGameView(CLIStateView cliStateView, ConnectionManager connectionManager) throws RemoteException {
        super();
        this.connectionManager = connectionManager;
        this.gameName = cliStateView.getGameName();
        this.token = cliStateView.getToken();
        this.clientCreateMessage = cliStateView.getClientCreateMessage();
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.cliStateView = cliStateView;
    }

    @Override
    public void onPlayersCreate(String jString) throws IOException {
        List<UserWrapper> users = clientGetMessage.getListOfUserWrapper(jString);
        users.forEach(user -> {
            try {
                connectionManager.getGameController().bindPlayer(
                        clientCreateMessage.createTokenMessage(token).createUsernameMessage(user.getUsername()).createGameNameMessage(gameName).buildMessage(),
                        new CLIPlayerView(cliStateView),
                        new CLISchemaCardView(cliStateView)
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
                    new CLIToolCardView(cliStateView, t.getName())
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
        clientGetMessage.getFrontBackSchemaCards(jString).forEach(c ->
            schemaCards.addAll(c.getSchemaCards())
        );


        for (int i = 0; i < schemaCards.size(); i++)
            buildGraphic.buildMessage("                   [" + (i + 1) + "]").buildGraphicSchemaCard(schemaCards.get(i));
        buildGraphic.buildMessage("Choose a schema card:");
        PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);

        connectionManager.getGameController().chosenSchemaCard(
                clientCreateMessage.createSchemaCardMessage(schemaCards.get(consoleListener.readNumber(schemaCards.size()))).
                        createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) {
        /*NOT IMPORTANT FOR THE CLI*/
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
                Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage) &&
                Objects.equals(token, that.token) &&
                Objects.equals(gameName, that.gameName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), connectionManager, cliStateView, clientGetMessage,
                clientCreateMessage, token, gameName);
    }


}
