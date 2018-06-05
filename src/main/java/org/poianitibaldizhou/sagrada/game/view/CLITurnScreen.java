package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.rmi.RemoteException;

public class CLITurnScreen extends CLIRoundScreen {

    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";

    /**
     * constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager  manager for handler the changed of the screen.
     * @throws RemoteException thrown when calling methods in a wrong sequence or passing invalid parameter values.
     */
    public CLITurnScreen(ConnectionManager networkManager, ScreenManager screenManager,CLIStateScreen cliStateScreen)
            throws RemoteException {
        super(networkManager, screenManager, cliStateScreen);

        initializeCommands();
    }

    @Override
    protected void initializeCommands() {
        super.initializeCommands();

        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card from Draft Pool");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);
    }


    private PositionWrapper selectPosition() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a position from your Schema Card").toString(),
                Level.STANDARD);
        viewMySchemaCard();
        return new PositionWrapper(consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_ROWS),
                consoleListener.readNumber(SchemaCardWrapper.NUMBER_OF_COLUMNS));
    }


    private void placeDice() {
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a dice to place in your schema card:").toString(),
                Level.STANDARD);
        viewDraftPool();
        int diceNumber = consoleListener.readNumber(draftPool.size());
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

    private void playToolCard(){
        BuildGraphic buildGraphic = new BuildGraphic();
        ConsoleListener consoleListener = ConsoleListener.getInstance();

        PrinterManager.consolePrint(buildGraphic.buildMessage("Choose a Tool Card:").
                buildGraphicToolCards(toolCardList).toString(), Level.STANDARD);
        ToolCardWrapper toolCardWrapper = toolCardList.get(consoleListener.readNumber(toolCardList.size()));
        try {
            connectionManager.getGameController().chooseAction(clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createActionMessage(new UseToolCardStateWrapper()).buildMessage()
            );
            connectionManager.getGameController().useToolCard( clientCreateMessage.createGameNameMessage(gameName).
                    createTokenMessage(token).createToolCardMessage(toolCardWrapper).buildMessage(),
                    new CLIToolCardExecutorView(cliStateScreen, toolCardWrapper.getName())
            );
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    BuildGraphic.NETWORK_ERROR, Level.ERROR);
        }
    }

}
