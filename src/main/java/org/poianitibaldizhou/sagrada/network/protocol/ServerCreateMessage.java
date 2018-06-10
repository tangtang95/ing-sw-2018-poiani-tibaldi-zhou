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
     * @return
     */
    public String getGameTerminatedErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.ERROR_TERMINATE_GAME_KEY, SharedConstants.ERROR_TEMINATE_GAME);
        return JSONObject.toJSONString(error);
    }

    public String reconnectErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.RECONNECT_ERROR);
        return JSONObject.toJSONString(error);
    }

    public ServerCreateMessage createTokenMessage(@NotNull String token) {
        jsonServerProtocol.appendMessage(SharedConstants.TOKEN_KEY, token);
        return this;
    }

    public ServerCreateMessage createDiceSwapMessage(@NotNull Dice oldDice,@NotNull  Dice newDice) {
        jsonServerProtocol.appendMessage(SharedConstants.OLD_DICE_KEY, oldDice);
        jsonServerProtocol.appendMessage(SharedConstants.NEW_DICE_KEY, newDice);
        return this;
    }

    public ServerCreateMessage createDiceList(@NotNull List<Dice> diceList) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_LIST_KEY,
                diceList);
        return this;
    }

    public ServerCreateMessage createTimeoutMessage(@NotNull String timeout) {
        jsonServerProtocol.appendMessage(SharedConstants.TIMEOUT_KEY, timeout);
        return this;
    }

    public <T> ServerCreateMessage createElem(@NotNull T elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM, elem);
        return this;
    }

    public <T> ServerCreateMessage createElemList(@NotNull List<T> elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM_LIST_KEY, elem);
        return this;
    }

    public ServerCreateMessage createUserList(@NotNull List<User> userList) {
        jsonServerProtocol.appendMessage(SharedConstants.USER_LIST_KEY, userList);
        return this;
    }

    public ServerCreateMessage createPublicObjectiveCardList(@NotNull List<PublicObjectiveCard> publicObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PUBLIC_OBJECTIVE_CARD_LIST_KEY, publicObjectiveCards);
        return this;
    }

    public ServerCreateMessage createToolCardList(@NotNull List<ToolCard> toolCards) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD_LIST_KEY, toolCards);
        return this;
    }

    public void clear() {
        jsonServerProtocol = new JSONProtocol();
    }

    public ServerCreateMessage createPrivateObjectiveCardList(@NotNull List<PrivateObjectiveCard> privateObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PRIVATE_OBJECTIVE_CARD_LIST_KEY, privateObjectiveCards);
        return this;
    }

    public ServerCreateMessage createFrontBackSchemaCardList(@NotNull List<FrontBackSchemaCard> frontBackSchemaCards) {
        jsonServerProtocol.appendMessage(SharedConstants.FRONT_CARD_LIST_KEY, frontBackSchemaCards);
        return this;
    }

    public ServerCreateMessage createMessageValue(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.INTEGER, value);
        return this;
    }

    public ServerCreateMessage createTurnValueMessage(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_VALUE_KEY, value);
        return this;
    }

    public ServerCreateMessage createOutcomeMessage(@NotNull Outcome outcome) {
        jsonServerProtocol.appendMessage(SharedConstants.OUTCOME, outcome);
        return this;
    }

    public ServerCreateMessage createUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.USER, user);
        return this;
    }

    public ServerCreateMessage createRoundUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_USER_KEY, user);
        return this;
    }

    public ServerCreateMessage createTurnUserMessage(@NotNull User user) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_USER_KEY, user);
        return this;
    }

    public ServerCreateMessage createBooleanMessage(@NotNull Boolean bool) {
        jsonServerProtocol.appendMessage(SharedConstants.BOOLEAN, bool);
        return this;
    }

    public ServerCreateMessage createPositionMessage(@NotNull Position position) {
        jsonServerProtocol.appendMessage(SharedConstants.POSITION, position);
        return this;
    }

    public ServerCreateMessage createVictoryPointMapMessage(@NotNull Map<User, Integer> map) {
        jsonServerProtocol.appendMessage(SharedConstants.VICTORY_POINT_MAP_KEY, map);
        return this;
    }

    public ServerCreateMessage createColorListMessage(@NotNull List<Color> colors) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR_LIST_KEY, colors);
        return this;
    }

    public ServerCreateMessage createDiceValueMessage(@NotNull Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_VALUE_KEY, value);
        return this;
    }

    public ServerCreateMessage createRoundTrackMessage(@NotNull RoundTrack roundTrack) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_TRACK, roundTrack);
        return this;
    }

    public ServerCreateMessage createColorMessage(@NotNull Color color) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR, color);
        return this;
    }

    public ServerCreateMessage createCommandFlowMessage(@NotNull CommandFlow commandFlow) {
        jsonServerProtocol.appendMessage(SharedConstants.COMMAND_FLOW, commandFlow);
        return this;
    }

    public ServerCreateMessage createGameNameMessage(@NotNull String gameName) {
        jsonServerProtocol.appendMessage(SharedConstants.GAME_NAME_KEY, gameName);
        return this;
    }

    public ServerCreateMessage createToolCardMessage(@NotNull ToolCard toolcard) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD, toolcard);
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

