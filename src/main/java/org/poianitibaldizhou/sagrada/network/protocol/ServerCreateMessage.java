package org.poianitibaldizhou.sagrada.network.protocol;

import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for creating a message to send to server.
 */
public class ServerCreateMessage {

    /**
     * network protocol.
     */
    private JSONProtocol jsonServerProtocol;

    /**
     * ServerCreateMessage constructor.
     */
    public ServerCreateMessage() {
        jsonServerProtocol = new JSONProtocol();
    }

    /**
     * Create a dice message.
     *
     * @param dice the dice to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createDiceMessage(@NotNull Dice dice){
        jsonServerProtocol.appendMessage(SharedConstants.DICE,dice);
        return this;
    }

    /**
     * Create a error message.
     *
     * @return the error message.
     */
    public String getErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.GET_ERROR);
        return JSONObject.toJSONString(error);
    }

    /**
     * Check if the game is terminated.
     *
     * @return the error message.
     */
    public String getGameTerminatedErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.ERROR_TERMINATE_GAME_KEY, SharedConstants.ERROR_TEMINATE_GAME);
        return JSONObject.toJSONString(error);
    }

    /**
     * Create a reconnect error message.
     *
     * @return a error message.
     */
    public String reconnectErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.RECONNECT_ERROR);
        return JSONObject.toJSONString(error);
    }

    /**
     * Create a token message.
     *
     * @param token the player's token to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createTokenMessage(@NotNull String token) {
        jsonServerProtocol.appendMessage(SharedConstants.TOKEN_KEY, token);
        return this;
    }

    /**
     * Create a diceSwap message.
     *
     * @param oldDice a old dice to send to client.
     * @param newDice a new dice to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createDiceSwapMessage(@NotNull Dice oldDice,@NotNull  Dice newDice) {
        jsonServerProtocol.appendMessage(SharedConstants.OLD_DICE_KEY, oldDice);
        jsonServerProtocol.appendMessage(SharedConstants.NEW_DICE_KEY, newDice);
        return this;
    }

    /**
     * Create a dice list message.
     *
     * @param diceList a dice list to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createDiceList(@NotNull List<Dice> diceList) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_LIST_KEY,
                diceList);
        return this;
    }

    /**
     * Create a timeout message.
     *
     * @param timeout the time to timeout to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createTimeoutMessage(@NotNull String timeout) {
        jsonServerProtocol.appendMessage(SharedConstants.TIMEOUT_KEY, timeout);
        return this;
    }

    /**
     * Create a elem message.
     *
     * @param elem elem message to send to client.
     * @param <T> generic elem.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public <T> ServerCreateMessage createElem(@NotNull T elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM, elem);
        return this;
    }

    /**
     * Create a list of elem.
     *
     * @param elem elem list to send to client.
     * @param <T> generic message.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public <T> ServerCreateMessage createElemList(@NotNull List<T> elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM_LIST_KEY, elem);
        return this;
    }

    /**
     * Create a list of user message,
     *
     * @param userList a list of user to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createUserList(@NotNull List<User> userList) {
        jsonServerProtocol.appendMessage(SharedConstants.USER_LIST_KEY, userList);
        return this;
    }

    /**
     * Create a list of publicObjectiveCards message.
     *
     * @param publicObjectiveCards a list of publicObjectiveCards to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createPublicObjectiveCardList(@NotNull List<PublicObjectiveCard> publicObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PUBLIC_OBJECTIVE_CARD_LIST_KEY, publicObjectiveCards);
        return this;
    }

    /**
     * Create a list of toolCard message.
     *
     * @param toolCards a list of toolCard to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createToolCardList(@NotNull List<ToolCard> toolCards) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD_LIST_KEY, toolCards);
        return this;
    }

    /**
     * Clear the packet.
     */
    public void clear() {
        jsonServerProtocol = new JSONProtocol();
    }

    /**
     * Create a list of privateObjectiveCards message.
     *
     * @param privateObjectiveCards a list of privateObjectiveCards to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createPrivateObjectiveCardList(@NotNull List<PrivateObjectiveCard> privateObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PRIVATE_OBJECTIVE_CARD_LIST_KEY, privateObjectiveCards);
        return this;
    }

    /**
     * Create a frontBackSchemaCard message.
     *
     * @param frontBackSchemaCards a list of frontBackSchemaCard to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createFrontBackSchemaCardList(@NotNull List<FrontBackSchemaCard> frontBackSchemaCards) {
        jsonServerProtocol.appendMessage(SharedConstants.FRONT_CARD_LIST_KEY, frontBackSchemaCards);
        return this;
    }

    /**
     * Create a value message.
     *
     * @param value a integer to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createMessageValue(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.INTEGER, value);
        return this;
    }

    /**
     * Create a turn value message.
     *
     * @param value a integer to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createTurnValueMessage(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_VALUE_KEY, value);
        return this;
    }

    /**
     * Create a outcome message.
     *
     * @param outcome a player's outcome to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createOutcomeMessage(@NotNull Outcome outcome) {
        jsonServerProtocol.appendMessage(SharedConstants.OUTCOME, outcome);
        return this;
    }

    /**
     * Create a user message.
     *
     * @param user a user to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.USER, user);
        return this;
    }

    /**
     * Create a round user message.
     *
     * @param user a user to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createRoundUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_USER_KEY, user);
        return this;
    }

    /**
     * Create a turn user message.
     *
     * @param user a user to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createTurnUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_USER_KEY, user);
        return this;
    }

    /**
     * Create a boolean message.
     *
     * @param bool a bool value to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createBooleanMessage(@NotNull Boolean bool) {
        jsonServerProtocol.appendMessage(SharedConstants.BOOLEAN, bool);
        return this;
    }

    /**
     * Create a position message.
     *
     * @param position the position to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createPositionMessage(@NotNull Position position) {
        jsonServerProtocol.appendMessage(SharedConstants.POSITION, position);
        return this;
    }

    /**
     * Create a victory point message.
     *
     * @param map a map with user and his victory point.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createVictoryPointMapMessage(@NotNull Map<User, Integer> map) {
        jsonServerProtocol.appendMessage(SharedConstants.VICTORY_POINT_MAP_KEY, map);
        return this;
    }

    /**
     * Create a list of color message.
     *
     * @param colors a list of color to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createColorListMessage(@NotNull List<Color> colors) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR_LIST_KEY, colors);
        return this;
    }

    /**
     * Create a dice value message.
     *
     * @param value a value of dice to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createDiceValueMessage(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_VALUE_KEY, value);
        return this;
    }

    /**
     * Create a roundTrack message.
     *
     * @param roundTrack a roundTrack to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createRoundTrackMessage(@NotNull RoundTrack roundTrack) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_TRACK, roundTrack);
        return this;
    }

    /**
     * Create a color message.
     *
     * @param color the color to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createColorMessage(@NotNull Color color) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR, color);
        return this;
    }

    /**
     * Create a commandFlow message.
     *
     * @param commandFlow a commandFlow to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createCommandFlowMessage(@NotNull CommandFlow commandFlow) {
        jsonServerProtocol.appendMessage(SharedConstants.COMMAND_FLOW, commandFlow);
        return this;
    }

    /**
     * Create a game name message.
     *
     * @param gameName the game name to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createGameNameMessage(@NotNull String gameName) {
        jsonServerProtocol.appendMessage(SharedConstants.GAME_NAME_KEY, gameName);
        return this;
    }

    /**
     * Create a toolCard message.
     *
     * @param toolCard a toolCard to send to client.
     * @return the ServerCreateMessage with the message adds to packet.
     */
    public ServerCreateMessage createToolCardMessage(@NotNull ToolCard toolCard) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD, toolCard);
        return this;
    }

    public ServerCreateMessage createSchemaCardMessage(@NotNull SchemaCard schemaCard) {
        jsonServerProtocol.appendMessage(SharedConstants.SCHEMA_CARD, schemaCard);
        return this;
    }


    public ServerCreateMessage createSchemaCardMapMessage(@NotNull Map<User, SchemaCard> stringSchemaCardMap) {
        jsonServerProtocol.appendMessage(SharedConstants.MAP_SCHEMA_CARD_KEY, stringSchemaCardMap);
        return this;
    }

    public ServerCreateMessage createDraftPoolMessage(@NotNull DraftPool draftPool) {
        jsonServerProtocol.appendMessage(SharedConstants.DRAFT_POOL, draftPool);
        return this;
    }

    public ServerCreateMessage createCoinsMessage(@NotNull Integer coins) {
        jsonServerProtocol.appendMessage(SharedConstants.INTEGER, coins);
        return this;
    }

    public ServerCreateMessage createPlayersCoinsMessage(@NotNull Map<User, Integer> playersCoins) {
        jsonServerProtocol.appendMessage(SharedConstants.MAP_PLAYERS_COINS_KEY, playersCoins);
        return this;
    }

    public String buildMessage() {
        String temp = jsonServerProtocol.buildMessage();
        jsonServerProtocol = new JSONProtocol();
        return temp;
    }

}

