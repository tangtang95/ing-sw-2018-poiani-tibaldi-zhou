package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The CLI of round, with the commands that the player can use when
 * he is not in his turn of play.
 */
public class CLIRoundScreen extends CLIBasicScreen {

    protected final String gameName;
    protected final String token;
    protected final transient UserWrapper myUser;
    protected transient List<ToolCardWrapper> toolCardList;
    protected transient DraftPoolWrapper draftPool;
    protected final transient CLIStateScreen cliStateScreen;

    protected final transient ClientGetMessage clientGetMessage;
    protected final transient ClientCreateMessage clientCreateMessage;


    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";
    private static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLIRoundScreen(ConnectionManager networkManager, ScreenManager screenManager, CLIStateScreen cliStateScreen)
            throws RemoteException {
        super(networkManager, screenManager);
        this.token = cliStateScreen.getToken();
        this.myUser = cliStateScreen.getMyUser();
        this.gameName = cliStateScreen.getGameName();
        this.clientCreateMessage = cliStateScreen.getClientCreateMessage();
        this.clientGetMessage = cliStateScreen.getClientGetMessage();
        this.cliStateScreen = cliStateScreen;

        initializeCommands();
    }


    @Override
    protected void initializeCommands() {
        Command viewDraftPool = new Command(VIEW_DRAFT_POOL, "View the game Draft Pool");
        viewDraftPool.setCommandAction(this::viewDraftPool);
        commandMap.put(viewDraftPool.getCommandText(), viewDraftPool);

        Command viewRoundTrack = new Command(VIEW_ROUND_TRACK, "View the game Round Track");
        viewRoundTrack.setCommandAction(this::viewRoundTrack);
        commandMap.put(viewRoundTrack.getCommandText(), viewRoundTrack);

        Command viewToolCards = new Command(VIEW_TOOL_CARDS, "View the Tool Cards playable");
        viewToolCards.setCommandAction(this::viewToolCards);
        commandMap.put(viewToolCards.getCommandText(), viewToolCards);

        Command viewPublicObjectiveCards = new Command(VIEW_PUBLIC_OBJECTIVE_CARD,
                "View the Public Objective cards");
        viewPublicObjectiveCards.setCommandAction(this::viewPublicObjectiveCards);
        commandMap.put(viewPublicObjectiveCards.getCommandText(), viewPublicObjectiveCards);

        Command viewSchemaCards = new Command(VIEW_SCHEMA_CARDS, "View the Schema card of all players");
        viewSchemaCards.setCommandAction(this::viewSchemaCards);
        commandMap.put(viewSchemaCards.getCommandText(), viewSchemaCards);

        Command viewMySchema = new Command(VIEW_MY_SCHEMA, "View my Schema card");
        viewMySchema.setCommandAction(this::viewMySchemaCard);
        commandMap.put(viewMySchema.getCommandText(), viewMySchema);

        Command viewPrivateObjectiveCards = new Command(VIEW_PRIVATE_OBJECTIVE_CARD,
                "View the Private Objective cards");
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(screenManager::popScreen);
        commandMap.put(quit.getCommandText(), quit);
    }

    @Override
    public void startCLI() {
        BuildGraphic buildGraphic = new BuildGraphic();

        PrinterManager.consolePrint(buildGraphic.
                        buildGraphicHelp(commandMap).
                        buildMessage("Choose the action: ").toString(),
                Level.STANDARD);
    }
    private void viewPrivateObjectiveCards(){
        BuildGraphic buildGraphic = new BuildGraphic();
        PrivateObjectiveCardWrapper poc = null;
        try {
            poc = clientGetMessage.getPrivateObjectiveCard(
                    connectionManager.getGameController().getPrivateObjectiveCardByToken(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPrivateObjectiveCard(poc).toString(),Level.STANDARD);
    }

    protected void viewMySchemaCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        SchemaCardWrapper schemaCard = null;
        try {
            schemaCard = clientGetMessage.getSchemaCard(
                    connectionManager.getGameController().getSchemaCardByToken(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicSchemaCard(schemaCard).toString(),Level.STANDARD);

    }

    private void viewSchemaCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        Map<UserWrapper,SchemaCardWrapper> schemaCards = null;
        try {
            schemaCards = clientGetMessage.getSchemaCards(
                    connectionManager.getGameController().getSchemaCards(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        Objects.requireNonNull(schemaCards).forEach((k, v) -> buildGraphic.buildMessage("Schema of Player:" + k).
                buildGraphicSchemaCard(v));
        PrinterManager.consolePrint(buildGraphic.toString(),Level.STANDARD);
    }

    private void viewPublicObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<PublicObjectiveCardWrapper> pocList = null;
        try {
            pocList = clientGetMessage.getPublicObjectiveCards(
                    connectionManager.getGameController().getPublicObjectiveCards(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPublicObjectiveCards(pocList).toString(),Level.STANDARD);
    }

    protected void viewToolCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<ToolCardWrapper> toolCardWrapperList = null;
        try {
            toolCardWrapperList = clientGetMessage.getToolCards(
                    connectionManager.getGameController().getToolCards(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        toolCardList = new ArrayList<>(Objects.requireNonNull(toolCardWrapperList));
        PrinterManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCardWrapperList).toString(),Level.STANDARD);
    }

    private void viewRoundTrack() {
        BuildGraphic buildGraphic = new BuildGraphic();
        RoundTrackWrapper roundTrackWrapper = null;
        try {
            roundTrackWrapper = clientGetMessage.getRoundTrack(
                    connectionManager.getGameController().getRoundTrack(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicRoundTrack(roundTrackWrapper).toString(),Level.STANDARD);
    }

    protected void viewDraftPool() {
        BuildGraphic buildGraphic = new BuildGraphic();
        DraftPoolWrapper draftPoolWrapper = null;
        try {
            draftPoolWrapper = clientGetMessage.getDraftPool(
                    connectionManager.getGameController().getDraftPool(
                            clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
                    ));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        draftPool = draftPoolWrapper;
        PrinterManager.consolePrint(buildGraphic.buildGraphicDraftPool(draftPoolWrapper).toString(),Level.STANDARD);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIRoundScreen)) return false;
        if (!super.equals(o)) return false;
        CLIRoundScreen that = (CLIRoundScreen) o;
        return Objects.equals(gameName, that.gameName) &&
                Objects.equals(myUser, that.myUser) &&
                Objects.equals(token, that.token) &&
                Objects.equals(clientGetMessage, that.clientGetMessage) &&
                Objects.equals(clientCreateMessage, that.clientCreateMessage);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), gameName, myUser, token, clientGetMessage, clientCreateMessage);
    }

    public static CLIRoundScreen reconnect() throws RemoteException{
        // TODO handle reconnection; not sure if here
        return null;
    }
}
