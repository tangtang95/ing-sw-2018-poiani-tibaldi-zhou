package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Strategy pattern for single player and multi player.
 */
public abstract class GameModeStrategy implements IScreen{

    /**
     * Map of the command for the CLI.
     */
    protected final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Reference at the game name of the current game.
     */
    protected final String gameName;

    /**
     * Reference at the player's token.
     */
    protected final String token;

    /**
     * ToolCard list with the toolCard available in game.
     */
    private List<ToolCardWrapper> toolCardList;

    /**
     * The game's draftPool
     */
    private DraftPoolWrapper draftPool;

    /**
     * Network manager for connecting with the server.
     */
    protected final ConnectionManager connectionManager;

    /**
     * Manager for handler the changed of the screen.
     */
    protected final ScreenManager screenManager;

    /**
     * Reference to ClientGetMessage for getting message from the server.
     */
    protected final ClientGetMessage clientGetMessage;

    /**
     * Reference to ClientCreateMessage for making the message to send at the server.
     */
    final ClientCreateMessage clientCreateMessage;

    /**
     * lock object for synchronizing with the turn start.
     */
    private final Object lock = CLIStateView.getLock();

    /**
     * Constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameName name of game.
     * @param token player's token.
     */
    GameModeStrategy(ConnectionManager connectionManager, ScreenManager screenManager, String gameName, String token ) {
        this.connectionManager = connectionManager;
        this.screenManager = screenManager;
        this.clientCreateMessage = new ClientCreateMessage();
        this.clientGetMessage = new ClientGetMessage();
        this.gameName = gameName;
        this.token = token;
    }

    /**
     * @return true if is a  single player mode.
     */
    public abstract boolean isSinglePlayer();

    /**
     * @return the game name.
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @return the player's token.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the connection manager.
     */
    public ConnectionManager getConnectionManager() {
        return connectionManager;
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
                            buildMessage(ClientMessage.CHOOSE_ACTION).toString(),
                    Level.STANDARD);
            consoleListener.setCommandMap(commandMap);
            CLIStateView.setStart(false);
            lock.notifyAll();
        }
    }

    /**
     * View my schema card command.
     * Print to screen the schema card of the player.
     */
    void viewMySchemaCard(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicSchemaCard(schemaCard).toString(), Level.STANDARD);
    }

    /**
     * View public objective cards command.
     * Print to screen the public objective cards of the game.
     */
    void viewPublicObjectiveCards(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPublicObjectiveCards(pocList).toString(), Level.STANDARD);
    }

    /**
     * View toolCards card command.
     * Print to screen the toolCards available in this game.
     */
    void viewToolCards(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        toolCardList = new ArrayList<>(Objects.requireNonNull(toolCardWrapperList));
        PrinterManager.consolePrint(buildGraphic.buildGraphicToolCards(toolCardWrapperList).toString(), Level.STANDARD);
    }

    /**
     * View roundTrack command.
     * Print to screen the game's roundTrack.
     */
    void viewRoundTrack(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicRoundTrack(roundTrackWrapper).toString(), Level.STANDARD);
    }

    /**
     * View draftPool command.
     * Print to screen the game's draft pool.
     */
    void viewDraftPool(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        draftPool = draftPoolWrapper;
        PrinterManager.consolePrint(buildGraphic.buildGraphicDraftPool(draftPoolWrapper).toString(), Level.STANDARD);
    }

    /**
     * View private objective cards command.
     * Print to screen the private objective cards of the player.
     */
    void viewPrivateObjectiveCards(){
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
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
        PrinterManager.consolePrint(buildGraphic.buildGraphicPrivateObjectiveCards(poc).toString(), Level.STANDARD);
    }

    /**
     * View the quit from the game.
     */
    void quit(){
        try {
            connectionManager.getGameController().quitGame(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
            PrinterManager.consolePrint(ClientMessage.YOU_LEFT_THE_GAME, Level.INFORMATION);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.FATAL_ERROR, Level.ERROR);
        }

        screenManager.popScreen();
    }

    /**
     * Select a position for placing a dice.
     *
     * @return a position choice.
     */
    private PositionWrapper selectPosition() throws TimeoutException {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_POSITION_ON_SCHEMA).
                        buildMessage(ClientMessage.CHOOSE_ROW).toString(),
                Level.STANDARD);
        try {
            int row = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS);

            PrinterManager.consolePrint(ClientMessage.CHOOSE_COLUMN, Level.STANDARD);
            int column = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS);

            return new PositionWrapper(row, column);
        } catch (TimeoutException e) {
            throw new TimeoutException();
        }
    }

    /**
     * place a dice on schema card.
     */
    void placeDice() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_DICE).toString(),
                Level.STANDARD);
        viewDraftPool();
        viewMySchemaCard();
        try {

            int diceNumber = consoleListener.readNumber(draftPool.size());
            PositionWrapper position = selectPosition();

            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new PlaceDiceActionWrapper()).buildMessage()
            );
            connectionManager.getGameController().placeDice(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createDiceMessage(draftPool.getDice(diceNumber)).
                    createPositionMessage(position).buildMessage()
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }

    }

    /**
     * play an available tool card.
     */
     void playToolCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        viewToolCards();
        PrinterManager.consolePrint(buildGraphic.buildMessage(ClientMessage.CHOOSE_TOOL_CARD).toString(), Level.STANDARD);
        try {
            ToolCardWrapper toolCardWrapper = toolCardList.get(consoleListener.readNumber(toolCardList.size()));

            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new UseToolCardActionWrapper()).buildMessage()
            );
            connectionManager.getGameController().useToolCard(clientCreateMessage.createGameNameMessage(gameName).
                            createTokenMessage(token).createToolCardMessage(toolCardWrapper).buildMessage(),
                    new CLIToolCardExecutorView(this, toolCardWrapper.getName())
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(ClientMessage.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

    /**
     * Finish the turn.
     */
     void endTurn() {
        try {
            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new EndTurnActionWrapper()).buildMessage()
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
        }
    }
}
