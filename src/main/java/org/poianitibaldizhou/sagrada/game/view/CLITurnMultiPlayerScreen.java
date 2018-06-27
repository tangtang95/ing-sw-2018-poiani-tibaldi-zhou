package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;

/**
 * The CLI of round, with the commands that the player can use when
 * he is in his turn of play.
 */
public class CLITurnMultiPlayerScreen extends CLIMultiPlayerScreen {

    /**
     * Constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameName name of game.
     * @param token player's token.
     */
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

        Command placeDice = new Command(ClientMessage.PLACE_DICE, ClientMessage.PLACE_DICE_HELP);
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(ClientMessage.PLAY_TOOL_CARD, ClientMessage.PLAY_TOOL_CARD_HELP);
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command endTurn = new Command(ClientMessage.END_TURN, ClientMessage.END_TURN_HELP);
        endTurn.setCommandAction(this::endTurn);
        commandMap.put(endTurn.getCommandText(), endTurn);

        Command quit = new Command(ClientMessage.QUIT_GAME, ClientMessage.QUIT_GAME_HELP);
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
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.YOU_LEFT_THE_GAME, Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.FATAL_ERROR, Level.ERROR);
        }
        screenManager.popWithoutStartInScreen();
        screenManager.popScreen();
    }

}
