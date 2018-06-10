package org.poianitibaldizhou.sagrada.game.view;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.*;

/**
 * The CLI of round, with the commands that the player can use when
 * he is not in his turn of play.
 */
public class CLIMultiPlayerScreen extends GameModeStrategy {

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
     * Constructor.
     *
     * @param networkManager the network manager for connecting with the server.
     * @param screenManager manager for handler the changed of the screen.
     * @param gameName name of game.
     * @param token player's token.
     */
    public CLIMultiPlayerScreen(ConnectionManager networkManager, ScreenManager screenManager, String gameName, String token){
        super(networkManager, screenManager, gameName, token);

        initializeCommands();
    }

    @Override
    public boolean isSinglePlayer() {
        return false;
    }

    /**
     * Initialize the ChangeConnection's commands.
     */
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
        quit.setCommandAction(this::quit);
        commandMap.put(quit.getCommandText(), quit);
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
     * @param o the other object to compare.
     * @return true if the CLIMultiPlayerScreen has the same gameName, user and token.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CLIMultiPlayerScreen)) return false;
        if (!super.equals(o)) return false;
        CLIMultiPlayerScreen that = (CLIMultiPlayerScreen) o;
        return Objects.equals(gameName, that.gameName) &&
                Objects.equals(token, that.token);
    }

    /**
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameName, token);
    }

}
