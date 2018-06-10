package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;

/**
 * The CLI of round, with the commands that the player can use when
 * he is in his turn of play.
 */
public class CLITurnMultiPlayerScreen extends CLIMultiPlayerScreen {

    /**
     * The commands that player can use when is his turn.
     */
    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String END_TURN = "End Turn";
    private static final String QUIT = "Quit game";


    CLITurnMultiPlayerScreen(ConnectionManager networkManager, ScreenManager screenManager,
                                    String gameName, String token) {
        super(networkManager, screenManager, gameName, token);

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
        try {
            connectionManager.getGameController().quitGame(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
            PrinterManager.consolePrint(this.getClass().getSimpleName() + "You have left the game.\n", Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + BuildGraphic.FATAL_ERROR, Level.ERROR);
        }
        screenManager.popWithoutStartInScreen();
        screenManager.popScreen();
    }

}
