package org.poianitibaldizhou.sagrada.game.view;

import edu.emory.mathcs.backport.java.util.concurrent.TimeoutException;
import javafx.print.Printer;
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

/**
 * This class implement the IGameView and it takes care
 * of printing the notify of game on-screen
 */
public class CLIGameView extends UnicastRemoteObject implements IGameView {

    /**
     * Network manager for connecting with the server.
     */
    private final transient ConnectionManager connectionManager;

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    private final transient CLIStateView cliStateView;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    private final transient ClientGetMessage clientGetMessage;

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    private final transient ClientCreateMessage clientCreateMessage;

    /**
     * Reference at the player's token.
     */
    private final transient String token;

    /**
     * Reference at the game name of the current game.
     */
    private final transient String gameName;

    /**
     * Constructor.
     *
     * @param cliStateView the CLI that contains all parameter.
     * @param connectionManager the network manager for connecting with the server.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIGameView(CLIStateView cliStateView, ConnectionManager connectionManager) throws RemoteException {
        super();
        this.connectionManager = connectionManager;
        this.gameName = cliStateView.getGameName();
        this.token = cliStateView.getToken();
        this.clientCreateMessage = cliStateView.getClientCreateMessage();
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.cliStateView = cliStateView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPlayersCreate(String jString) throws IOException {
        List<UserWrapper> users = clientGetMessage.getListOfUserWrapper(jString);
        users.forEach(user -> {
            try {
                connectionManager.getGameController().bindPlayer(
                        clientCreateMessage.createTokenMessage(token).createUsernameMessage(user.getUsername()).
                                createGameNameMessage(gameName).buildMessage(),
                        new CLIPlayerView(cliStateView),
                        new CLISchemaCardView(cliStateView, user.getUsername())
                );
            } catch (IOException e) {
                PrinterManager.consolePrint(this.getClass().getSimpleName() +
                        BuildGraphic.NETWORK_ERROR, Level.ERROR);
            }
        });

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPublicObjectiveCardsDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                buildMessage("Public objective cards valid for this game: ").
                buildGraphicPublicObjectiveCards(clientGetMessage.getPublicObjectiveCards(jString)).toString(),
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChoosePrivateObjectiveCards(String privateObjectiveCards) {
        /*SINGLE PLAYER*/
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPrivateObjectiveCardDraw(String jString) throws IOException {
        BuildGraphic buildGraphic = new BuildGraphic();
        PrinterManager.consolePrint(buildGraphic.
                        buildMessage("Private objective cards valid for this game: ").
                        buildGraphicPrivateObjectiveCards(clientGetMessage.getPrivateObjectiveCards(jString)).toString(),
                Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
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

        try {
            connectionManager.getGameController().chosenSchemaCard(
                    clientCreateMessage.createSchemaCardMessage(schemaCards.get(consoleListener.readNumber(schemaCards.size()))).
                            createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION,Level.INFORMATION);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ack(String ack) {
        PrinterManager.consolePrint(ack + "\n", Level.STANDARD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void err(String err) {
        PrinterManager.consolePrint(err + "\n", Level.ERROR);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIStateView is the same or the token is the same and the game name is the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIGameView)) return false;
        if (!super.equals(o)) return false;
        CLIGameView that = (CLIGameView) o;
        return Objects.equals(cliStateView, that.cliStateView) &&
                Objects.equals(token, that.token) &&
                Objects.equals(gameName, that.gameName);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return this.getClass().getSimpleName().hashCode();
    }


}
