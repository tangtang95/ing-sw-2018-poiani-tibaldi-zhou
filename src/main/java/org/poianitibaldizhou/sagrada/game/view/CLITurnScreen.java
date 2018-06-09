package org.poianitibaldizhou.sagrada.game.view;

import edu.emory.mathcs.backport.java.util.concurrent.TimeoutException;
import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * The CLI of round, with the commands that the player can use when
 * he is in his turn of play.
 */
public class CLITurnScreen extends CLIRoundScreen {

    /**
     * The commands that player can use when is his turn.
     */
    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String END_TURN = "End Turn";
    private static final String QUIT = "Quit game";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    CLITurnScreen(ConnectionManager networkManager, ScreenManager screenManager, CLIStateView cliStateView)
            throws RemoteException {
        super(networkManager, screenManager, cliStateView);

        initializeCommands();
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
    @Override
    protected void initializeCommands() {
        super.initializeCommands();

        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card from Draft Pool");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command endTurn = new Command(END_TURN, "End the turn");
        endTurn.setCommandAction(this::endTurn);
        commandMap.put(endTurn.getCommandText(), endTurn);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(this::quitGame);
        commandMap.put(quit.getCommandText(), quit);
    }

    /**
     * Quit the game
     */
    private void quitGame() {
        endTurn();
        try {
            connectionManager.getGameController().quitGame(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
            PrinterManager.consolePrint(this.getClass().getSimpleName() + "You have left the game.\n", Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.FATAL_ERROR, Level.ERROR);
        }

        screenManager.popWithouthStartinScreen();
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

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").
                        buildMessage("Choose a row:").toString(),
                Level.STANDARD);
        try {
            int row = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS);

            PrinterManager.consolePrint("Choose a column:\n", Level.STANDARD);
            int column = consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS);

            return new PositionWrapper(row, column);
        } catch (TimeoutException e) {
            throw new TimeoutException();
        }
    }

    /**
     * Finish the turn.
     */
    private void endTurn() {
        try {
            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new EndTurnStateWrapper()).buildMessage()
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

    /**
     * place a dice on schema card.
     */
    private void placeDice() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a dice to place in your schema card:").toString(),
                Level.STANDARD);
        viewDraftPool();
        viewMySchemaCard();
        try {

            int diceNumber = consoleListener.readNumber(draftPool.size());
            PositionWrapper position = selectPosition();

            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new PlaceDiceStateWrapper()).buildMessage()
            );
            connectionManager.getGameController().placeDice(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createDiceMessage(draftPool.getDice(diceNumber)).
                    createPositionMessage(position).buildMessage()
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
        }

    }

    /**
     * play an available tool card.
     */
    private void playToolCard() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        viewToolCards();
        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a Tool Card:").toString(), Level.STANDARD);
        try {
            ToolCardWrapper toolCardWrapper = toolCardList.get(consoleListener.readNumber(toolCardList.size()));

            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new UseToolCardStateWrapper()).buildMessage()
            );
            connectionManager.getGameController().useToolCard(clientCreateMessage.createGameNameMessage(gameName).
                            createTokenMessage(token).createToolCardMessage(toolCardWrapper).buildMessage(),
                    new CLIToolCardExecutorView(cliStateView, toolCardWrapper.getName())
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        } catch (TimeoutException e) {
            PrinterManager.consolePrint(BuildGraphic.AUTOMATIC_ACTION, Level.INFORMATION);
        }
    }

}
