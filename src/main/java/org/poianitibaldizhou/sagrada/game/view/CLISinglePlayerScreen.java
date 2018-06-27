package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;

public class CLISinglePlayerScreen extends GameModeStrategy {

    /**
     * CLISinglePlayerScreen commands.
     */
    private static final String QUIT = "Quit game";
    private static final String VIEW_DRAFT_POOL = "View the Draft Pool";
    private static final String VIEW_ROUND_TRACK = "View the Round Track";
    private static final String VIEW_TOOL_CARDS = "View the list of Tool Card";
    private static final String VIEW_PUBLIC_OBJECTIVE_CARD = "View the public objective cards";
    private static final String VIEW_MY_SCHEMA = "View my schema Card";
    private static final String VIEW_PRIVATE_OBJECTIVE_CARD = "View the private objective cards";
    private static final String PLACE_DICE = "Place dice";
    private static final String PLAY_TOOL_CARD = "Play Tool Card";
    private static final String END_TURN = "End Turn";


    /**
     * Constructor.
     *
     * @param connectionManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameName name of game.
     * @param token player's token.
     */
    CLISinglePlayerScreen(ConnectionManager connectionManager, ScreenManager screenManager,
                                 String gameName, String token) {
        super(connectionManager,screenManager,gameName,token);
        initializeCommands();
    }

    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    private void initializeCommands() {
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

        Command viewMySchema = new Command(VIEW_MY_SCHEMA, "View my Schema card");
        viewMySchema.setCommandAction(this::viewMySchemaCard);
        commandMap.put(viewMySchema.getCommandText(), viewMySchema);

        Command viewPrivateObjectiveCards = new Command(VIEW_PRIVATE_OBJECTIVE_CARD,
                "View the Private Objective cards");
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);

        Command quit = new Command(QUIT, "Quit from current game");
        quit.setCommandAction(this::quitGame);
        commandMap.put(quit.getCommandText(), quit);

        Command placeDice = new Command(PLACE_DICE, "Place a dice on Schema Card from Draft Pool");
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(PLAY_TOOL_CARD, "Play a Tool Card");
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command endTurn = new Command(END_TURN, "End the turn");
        endTurn.setCommandAction(this::endTurn);
        commandMap.put(endTurn.getCommandText(), endTurn);
    }

    /**
     * Quit the game
     */
    private void quitGame() {
        try {
            connectionManager.getGameController().quitGame(
                    clientCreateMessage.createTokenMessage(token).createGameNameMessage(gameName).buildMessage()
            );
            PrinterManager.consolePrint("You have left the game.\n", Level.INFORMATION);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.FATAL_ERROR, Level.ERROR);
        }
        screenManager.popWithoutStartInScreen();
        screenManager.popScreen();
    }

}
