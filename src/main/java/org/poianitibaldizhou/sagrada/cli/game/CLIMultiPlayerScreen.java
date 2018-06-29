package org.poianitibaldizhou.sagrada.cli.game;

import org.poianitibaldizhou.sagrada.cli.*;
import org.poianitibaldizhou.sagrada.network.ConnectionManager;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;
import org.poianitibaldizhou.sagrada.utilities.ClientMessage;

import java.io.IOException;
import java.util.*;

/**
 * The CLI of round, with the commands that the player can use when
 * he is not in his turn of play.
 */
public class CLIMultiPlayerScreen extends GameModeStrategy {

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

        Command viewSchemaCards = new Command(ClientMessage.VIEW_SCHEMA_CARDS, ClientMessage.VIEW_SCHEMA_CARDS_HELP);
        viewSchemaCards.setCommandAction(this::viewSchemaCards);
        commandMap.put(viewSchemaCards.getCommandText(), viewSchemaCards);

        Command viewMySchema = new Command(ClientMessage.VIEW_MY_SCHEMA, ClientMessage.VIEW_MY_SCHEMA_HELP);
        viewMySchema.setCommandAction(this::viewMySchemaCard);
        commandMap.put(viewMySchema.getCommandText(), viewMySchema);

        Command viewMyCoins = new Command(ClientMessage.VIEW_MY_COINS, ClientMessage.VIEW_MY_COINS_HELP);
        viewMyCoins.setCommandAction(this::viewMyCoins);
        commandMap.put(viewMyCoins.getCommandText(), viewMyCoins);

        Command viewPlayersCoins = new Command(ClientMessage.VIEW_PLAYERS_COINS, ClientMessage.VIEW_PLAYERS_COINS_HELP);
        viewPlayersCoins.setCommandAction(this::viewPlayersCoins);
        commandMap.put(viewPlayersCoins.getCommandText(), viewPlayersCoins);

        Command viewPrivateObjectiveCards = new Command(ClientMessage.VIEW_PRIVATE_OBJECTIVE_CARD,
                ClientMessage.VIEW_PRIVATE_OBJECTIVE_CARD_HELP);
        viewPrivateObjectiveCards.setCommandAction(this::viewPrivateObjectiveCards);
        commandMap.put(viewPrivateObjectiveCards.getCommandText(), viewPrivateObjectiveCards);

        Command viewTimeToTimeout = new Command(ClientMessage.VIEW_TIME_TO_TIMEOUT, ClientMessage.VIEW_TIME_TO_TIMEOUT_HELP);
        viewTimeToTimeout.setCommandAction(this::viewTimeToTimeout);
        commandMap.put(viewTimeToTimeout.getCommandText(), viewTimeToTimeout);

        Command quit = new Command(ClientMessage.QUIT_GAME, ClientMessage.QUIT_GAME_HELP);
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
                PrinterManager.consolePrint(ClientMessage.TIME_TO_TIMEOUT + timeout, Level.STANDARD);
            }
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() + ClientMessage.NETWORK_ERROR, Level.ERROR);
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
            PrinterManager.consolePrint(ClientMessage.YOUR_COINS + coins + "\n", Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
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
                    PrinterManager.consolePrint(String.format(ClientMessage.COINS_OF,k.getUsername(),v.toString()),
                            Level.STANDARD));
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
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
            Objects.requireNonNull(schemaCards).forEach((k, v) -> buildGraphic.buildMessage(
                    ClientMessage.SCHEMA_CARD_OF+ k).
                    buildGraphicSchemaCard(v));
            PrinterManager.consolePrint(buildGraphic.toString(), Level.STANDARD);
        } catch (IOException e) {
            PrinterManager.consolePrint(this.getClass().getSimpleName() +
                    ClientMessage.NETWORK_ERROR, Level.ERROR);
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
