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

    /**
     * Reference at the game name of the current game.
     */
    protected final String gameName;

    /**
     * Reference at the player's token.
     */
    protected final String token;

    /**
     * Reference at my User with my userName.
     */
    private final transient UserWrapper myUser;

    /**
     * ToolCard list with the toolCard available in game.
     */
    transient List<ToolCardWrapper> toolCardList;

    /**
     * The game's draftPool
     */
    protected transient DraftPoolWrapper draftPool;

    /**
     * Reference to CLIStateView for passing the parameter.
     */
    protected final transient CLIStateView cliStateView;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    protected final transient ClientGetMessage clientGetMessage;

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    final transient ClientCreateMessage clientCreateMessage;

    /**
     * lock object for synchronizing with the turn start.
     */
    private final transient Object lock;

    /**
     * The commands that player can use when is not his turn.
     */
    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_SCHEMA_CARDS = "View Schema Cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";
    private static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";
    private static final String VIEW_MY_COINS = "View my coins";
    private static final String VIEW_PLAYERS_COINS = "View players coins";
    private static final String VIEW_TIME_TO_TIMEOUT = "View time to timeout";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLIRoundScreen(ConnectionManager networkManager, ScreenManager screenManager, CLIStateView cliStateView)
            throws RemoteException {
        super(networkManager, screenManager);
        this.token = cliStateView.getToken();
        this.myUser = cliStateView.getMyUser();
        this.gameName = cliStateView.getGameName();
        this.clientCreateMessage = cliStateView.getClientCreateMessage();
        this.clientGetMessage = cliStateView.getClientGetMessage();
        this.cliStateView = cliStateView;
        this.lock = cliStateView.getLock();

        initializeCommands();
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
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

        Command viewMyCoins = new Command(VIEW_MY_COINS, "View my expendable coins");
        viewMyCoins.setCommandAction(this::viewMyCoins);
        commandMap.put(viewMyCoins.getCommandText(), viewMyCoins);

        Command viewPlayersCoins = new Command(VIEW_PLAYERS_COINS, "View the coins of all players");
        viewPlayersCoins.setCommandAction(this::viewPlayersCoins);
        commandMap.put(viewPlayersCoins.getCommandText(), viewPlayersCoins);

        Command viewPrivateObjectiveCards = new Command(VIEW_PRIVATE_OBJECTIVE_CARD,
                "View the Private Objective cards");
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);

        Command viewTimeToTimeout = new Command(VIEW_TIME_TO_TIMEOUT, "View time to time out");
        viewTimeToTimeout.setCommandAction(this::viewTimeToTimeout);
        commandMap.put(viewTimeToTimeout.getCommandText(), viewTimeToTimeout);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(this::viewQuitScreen);
        commandMap.put(quit.getCommandText(), quit);
    }

    /**
     * Start the CLI.
     */
    @Override
    public synchronized void startCLI() {
        synchronized (lock) {
            ConsoleListener consoleListener = ConsoleListener.getInstance();
            BuildGraphic buildGraphic = new BuildGraphic();

            PrinterManager.consolePrint(buildGraphic.
                            buildGraphicHelp(commandMap).
                            buildMessage("Choose the action: ").toString(),
                    Level.STANDARD);
            consoleListener.setCommandMap(commandMap);
            cliStateView.setStart(false);
            lock.notifyAll();
        }
    }

    /**
     * View the quit from the game.
     */
    private void viewQuitScreen() {
        try {
            connectionManager.getGameController().quitGame(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
            PrinterManager.consolePrint(this.getClass().getSimpleName() + "You have left the game.\n", Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.FATAL_ERROR, Level.ERROR);
        }

        screenManager.popScreen();
    }

    /**
     * View time to time out
     */
    private void viewTimeToTimeout() {
        String timeout;
        String response;
        try {
            response = connectionManager.getGameController().getTimeout(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage());
            if (!clientGetMessage.hasTerminateGameError(response)) {
                timeout = clientGetMessage.getTimeout(response);
                PrinterManager.consolePrint("Time to timeout is: " + timeout, Level.STANDARD);
            }
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * View my coins command.
     * Print to screen the number of expendable coin of the player.
     */
    private void viewMyCoins() {
        Integer coins;
        try {
            String response = connectionManager.getGameController().getMyCoins(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            coins = clientGetMessage.getMyCoins(response);
            PrinterManager.consolePrint("Your coins: " + coins + "\n", Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * View players coins command.
     * Print to screen the number of expendable coin of all players in game.
     */
    private void viewPlayersCoins() {
        Map<UserWrapper, Integer> playersCoins;
        try {
            String response = connectionManager.getGameController().getPlayersCoins(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            playersCoins = clientGetMessage.getPlayersCoins(response);
            playersCoins.forEach((k, v) ->
                    PrinterManager.consolePrint("Coins of " + k.getUsername() + ": " + v.toString() + "\n",
                            Level.STANDARD));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * View private objective cards command.
     * Print to screen the private objective cards of the player.
     */
    private void viewPrivateObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<PrivateObjectiveCardWrapper> poc = new ArrayList<>();
        try {
            String response = connectionManager.getGameController().getPrivateObjectiveCardByToken(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            poc = clientGetMessage.getPrivateObjectiveCards(response);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPrivateObjectiveCards(poc).toString(), Level.STANDARD);
    }

    /**
     * View my schema card command.
     * Print to screen the schema card of the player.
     */
    void viewMySchemaCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        SchemaCardWrapper schemaCard = null;
        try {
            String response = connectionManager.getGameController().getSchemaCardByToken(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if (clientGetMessage.hasTerminateGameError(response))
                return;
            schemaCard = clientGetMessage.getSchemaCard(response);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicSchemaCard(schemaCard).toString(), Level.STANDARD);

    }

    /**
     * View players schema cards command.
     * Print to screen the schema cards of all players in game.
     */
    private void viewSchemaCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        Map<UserWrapper, SchemaCardWrapper> schemaCards;
        try {
            String response = connectionManager.getGameController().getSchemaCards(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            schemaCards = clientGetMessage.getSchemaCards(response);
            Objects.requireNonNull(schemaCards).forEach((k, v) -> buildGraphic.buildMessage("Schema of Player:" + k).
                    buildGraphicSchemaCard(v));
            PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * View public objective cards command.
     * Print to screen the public objective cards of the game.
     */
    private void viewPublicObjectiveCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<PublicObjectiveCardWrapper> pocList = null;
        try {
            String response = connectionManager.getGameController().getPublicObjectiveCards(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            pocList = clientGetMessage.getPublicObjectiveCards(response);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPublicObjectiveCards(pocList).toString(), Level.STANDARD);
    }

    /**
     * View toolCards card command.
     * Print to screen the toolCards available in this game.
     */
    void viewToolCards() {
        BuildGraphic buildGraphic = new BuildGraphic();
        List<ToolCardWrapper> toolCardWrapperList = null;
        try {
            String response = connectionManager.getGameController().getToolCards(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            toolCardWrapperList = clientGetMessage.getToolCards(response);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        toolCardList = new ArrayList<>(Objects.requireNonNull(toolCardWrapperList));
        PrinterManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCardWrapperList).toString(), Level.STANDARD);
    }

    /**
     * View roundTrack command.
     * Print to screen the game's roundTrack.
     */
    private void viewRoundTrack() {
        BuildGraphic buildGraphic = new BuildGraphic();
        RoundTrackWrapper roundTrackWrapper = null;
        try {
            String response = connectionManager.getGameController().getRoundTrack(
                    clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage()
            );
            if(clientGetMessage.hasTerminateGameError(response))
                return;
            roundTrackWrapper = clientGetMessage.getRoundTrack(response);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicRoundTrack(roundTrackWrapper).toString(), Level.STANDARD);
    }

    /**
     * View draftPool command.
     * Print to screen the game's draft pool.
     */
    void viewDraftPool() {
        BuildGraphic buildGraphic = new BuildGraphic();
        DraftPoolWrapper draftPoolWrapper = null;
        try {
            String response = connectionManager.getGameController().getDraftPool(clientCreateMessage.createGameNameMessage(gameName).createTokenMessage(token).buildMessage());
            if(!clientGetMessage.hasTerminateGameError(response))
                draftPoolWrapper = clientGetMessage.getDraftPool(response);
            else
                return;
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
        draftPool = draftPoolWrapper;
        PrinterManager.consolePrint(buildGraphic.buildGraphicDraftPool(draftPoolWrapper).toString(), Level.STANDARD);
    }

    /**
     * @param o the other object to compare.
     * @return true if the CLIRoundScreen has the same gameName, user and token.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIRoundScreen)) return false;
        if (!super.equals(o)) return false;
        CLIRoundScreen that = (CLIRoundScreen) o;
        return Objects.equals(gameName, that.gameName) &&
                Objects.equals(myUser, that.myUser) &&
                Objects.equals(token, that.token);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameName, myUser, token);
    }

}
