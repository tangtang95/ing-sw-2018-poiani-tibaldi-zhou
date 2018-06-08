package org.poianitibaldizhou.sagrada.game.view;

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
    public CLITurnScreen(ConnectionManager networkManager, ScreenManager screenManager,CLIStateView cliStateView)
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
        quit.setCommandAction(() -> {
            screenManager.popScreen();
            screenManager.popScreen();
        });
        commandMap.put(quit.getCommandText(), quit);
    }

    /**
     * Select a position for placing a dice.
     *
     * @return a position choice.
     */
    private PositionWrapper selectPosition() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").
                        buildMessage("Choose a row:").toString(),
                Level.STANDARD);
        int row = consoleListener.readPositionNumber(SchemaCardWrapper.NUMBER_OF_ROWS);
        PrinterManager.consolePrint("Choose a column:\n", Level.STANDARD);
        int column = consoleListener.readPositionNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS);
        return new PositionWrapper(row, column);
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
        int diceNumber = consoleListener.readPositionNumber(draftPool.size());
        PositionWrapper position = selectPosition();
        try {
            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new PlaceDiceStateWrapper()).buildMessage()
            );
            connectionManager.getGameController().placeDice( clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createDiceMessage(draftPool.getDice(diceNumber)).
                    createPositionMessage(position).buildMessage()
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }

    }

    /**
     * play an available tool card.
     */
    private void playToolCard(){
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        viewToolCards();
        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a Tool Card:").toString(), Level.STANDARD);
        ToolCardWrapper toolCardWrapper = toolCardList.get(consoleListener.readPositionNumber(toolCardList.size()));
        try {
            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new UseToolCardStateWrapper()).buildMessage()
            );
            connectionManager.getGameController().useToolCard( clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createToolCardMessage(toolCardWrapper).buildMessage(),
                    new CLIToolCardExecutorView(cliStateView, toolCardWrapper.getName())
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

}
