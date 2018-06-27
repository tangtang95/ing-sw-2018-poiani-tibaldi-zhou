package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;

public class CLISinglePlayerScreen extends GameModeStrategy {

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
        Command viewDraftPool = new Command(ClientMessage.VIEW_DRAFT_POOL, ClientMessage.VIEW_DRAFT_POOL_HELP);
        viewDraftPool.setCommandAction(this::viewDraftPool);
        commandMap.put(viewDraftPool.getCommandText(), viewDraftPool);

        Command viewRoundTrack = new Command(ClientMessage.VIEW_ROUND_TRACK, ClientMessage.VIEW_ROUND_TRACK_HELP);
        viewRoundTrack.setCommandAction(this::viewRoundTrack);
        commandMap.put(viewRoundTrack.getCommandText(), viewRoundTrack);

        Command viewToolCards = new Command(ClientMessage.VIEW_TOOL_CARDS, ClientMessage.VIEW_TOOL_CARDS_HELP);
        viewToolCards.setCommandAction(this::viewToolCards);
        commandMap.put(viewToolCards.getCommandText(), viewToolCards);

        Command viewPublicObjectiveCards = new Command(ClientMessage.VIEW_PUBLIC_OBJECTIVE_CARD,
                ClientMessage.VIEW_PUBLIC_OBJECTIVE_CARD_HELP);
        viewPublicObjectiveCards.setCommandAction(this::viewPublicObjectiveCards);
        commandMap.put(viewPublicObjectiveCards.getCommandText(), viewPublicObjectiveCards);

        Command viewMySchema = new Command(ClientMessage.VIEW_MY_SCHEMA, ClientMessage.VIEW_MY_SCHEMA_HELP);
        viewMySchema.setCommandAction(this::viewMySchemaCard);
        commandMap.put(viewMySchema.getCommandText(), viewMySchema);

        Command viewPrivateObjectiveCards = new Command(ClientMessage.VIEW_PRIVATE_OBJECTIVE_CARD,
                ClientMessage.VIEW_PRIVATE_OBJECTIVE_CARD_HELP);
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);

        Command quit = new Command(ClientMessage.QUIT_GAME, ClientMessage.QUIT_GAME_HELP);
        quit.setCommandAction(this::quitGame);
        commandMap.put(quit.getCommandText(), quit);

        Command placeDice = new Command(ClientMessage.PLACE_DICE, ClientMessage.PLACE_DICE_HELP);
        placeDice.setCommandAction(this::placeDice);
        commandMap.put(placeDice.getCommandText(), placeDice);

        Command playToolCard = new Command(ClientMessage.PLAY_TOOL_CARD, ClientMessage.PLAY_TOOL_CARD_HELP);
        playToolCard.setCommandAction(this::playToolCard);
        commandMap.put(playToolCard.getCommandText(), playToolCard);

        Command endTurn = new Command(ClientMessage.END_TURN, ClientMessage.END_TURN_HELP);
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
            PrinterManager.consolePrint(ClientMessage.YOU_LEFT_THE_GAME, Level.INFORMATION);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.FATAL_ERROR, Level.ERROR);
        }
        screenManager.popWithoutStartInScreen();
        screenManager.popScreen();
    }

}
