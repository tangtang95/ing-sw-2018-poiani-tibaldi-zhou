package org.poianitibaldizhou.sagrada.network.protocol;

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

import java.util.List;
import java.util.Map;

public class ServerCreateMessage {

    private JSONProtocol jsonServerProtocol;

    public ServerCreateMessage() {
        jsonServerProtocol = new JSONProtocol();
    }

    public ServerCreateMessage createDiceMessage(Dice dice){
        jsonServerProtocol.appendMessage(SharedConstants.DICE,dice);
        return this;
    }

    public ServerCreateMessage createTokenMessage(String token) {
        jsonServerProtocol.appendMessage(SharedConstants.TOKEN_KEY, token);
        return this;
    }

    public ServerCreateMessage createDiceSwapMessage(Dice oldDice, Dice newDice) {
        jsonServerProtocol.appendMessage(SharedConstants.OLD_DICE_KEY, oldDice);
        jsonServerProtocol.appendMessage(SharedConstants.NEW_DICE_KEY, newDice);
        return this;
    }

    public ServerCreateMessage createDiceList(List<Dice> diceList) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_LIST_KEY,
                diceList);
        return this;
    }

    public ServerCreateMessage createTimeoutMessage(String timeout) {
        jsonServerProtocol.appendMessage(SharedConstants.TIMEOUT_KEY, timeout);
        return this;
    }

    public <T> ServerCreateMessage createElem(T elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM, elem);
        return this;
    }

    public <T> ServerCreateMessage createElemList(List<T> elem) {
        jsonServerProtocol.appendMessage(SharedConstants.ELEM_LIST_KEY, elem);
        return this;
    }

    public ServerCreateMessage createUserList(List<User> userList) {
        jsonServerProtocol.appendMessage(SharedConstants.USER_LIST_KEY, userList);
        return this;
    }

    public ServerCreateMessage createPublicObjectiveCardList(List<PublicObjectiveCard> publicObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PUBLIC_OBJECTIVE_CARD_LIST_KEY, publicObjectiveCards);
        return this;
    }

    public ServerCreateMessage createToolCardList(List<ToolCard> toolcards) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD_LIST_KEY, toolcards);
        return this;
    }

    public void clear() {
        jsonServerProtocol = new JSONProtocol();
    }

    public ServerCreateMessage createPrivateObjectiveCardList(List<PrivateObjectiveCard> privateObjectiveCards) {
        jsonServerProtocol.appendMessage(SharedConstants.PRIVATE_OBJECTIVE_CARD_LIST_KEY, privateObjectiveCards);
        return this;
    }

    public ServerCreateMessage createFrontBackSchemaCardList(List<FrontBackSchemaCard> frontBackSchemaCards) {
        jsonServerProtocol.appendMessage(SharedConstants.FRONT_CARD_LIST_KEY, frontBackSchemaCards);
        return this;
    }

    public ServerCreateMessage createMessageValue(Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.INTEGER, value);
        return this;
    }

    public ServerCreateMessage createTurnValueMessage(Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_VALUE_KEY, value);
        return this;
    }

    public ServerCreateMessage createOutcomeMessage(Outcome outcome) {
        jsonServerProtocol.appendMessage(SharedConstants.OUTCOME, outcome);
        return this;
    }

    public ServerCreateMessage createUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.USER, user);
        return this;
    }

    public ServerCreateMessage createRoundUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_USER_KEY, user);
        return this;
    }

    public ServerCreateMessage createTurnUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_USER_KEY, user);
        return this;
    }

    public ServerCreateMessage createBooleanMessage(Boolean bool) {
        jsonServerProtocol.appendMessage(SharedConstants.BOOLEAN, bool);
        return this;
    }

    public ServerCreateMessage createPositionMessage(Position position) {
        jsonServerProtocol.appendMessage(SharedConstants.POSITION, position);
        return this;
    }

    public ServerCreateMessage createVictoryPointMapMessage(Map<User, Integer> map) {
        jsonServerProtocol.appendMessage(SharedConstants.VICTORY_POINT_MAP_KEY, map);
        return this;
    }

    public ServerCreateMessage createColorListMessage(List<Color> colors) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR_LIST_KEY, colors);
        return this;
    }

    public ServerCreateMessage createDiceValueMessage(Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_VALUE_KEY, value);
        return this;
    }

    public ServerCreateMessage createRoundTrackMessage(RoundTrack roundTrack) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_TRACK, roundTrack);
        return this;
    }

    public ServerCreateMessage createColorMessage(Color color) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR, color);
        return this;
    }

    public ServerCreateMessage createCommandFlowMessage(CommandFlow commandFlow) {
        jsonServerProtocol.appendMessage(SharedConstants.COMMAND_FLOW, commandFlow);
        return this;
    }

    public ServerCreateMessage createGamenNameMessage(String gameName) {
        jsonServerProtocol.appendMessage(SharedConstants.GAME_NAME_KEY, gameName);
        return this;
    }

    public ServerCreateMessage createToolCardMessage(ToolCard toolcard) {
        jsonServerProtocol.appendMessage(SharedConstants.TOOL_CARD, toolcard);
        return this;
    }

    public ServerCreateMessage createSchemaCardMessage(SchemaCard schemaCard) {
        jsonServerProtocol.appendMessage(SharedConstants.SCHEMA_CARD, schemaCard);
        return this;
    }


    public ServerCreateMessage createSchemaCardMapMessage(Map<User, SchemaCard> stringSchemaCardMap) {
        jsonServerProtocol.appendMessage(SharedConstants.MAP_SCHEMA_CARD_KEY, stringSchemaCardMap);
        return this;
    }

    public ServerCreateMessage createDraftPoolMessage(DraftPool draftPool) {
        jsonServerProtocol.appendMessage(SharedConstants.DRAFT_POOL, draftPool);
        return this;
    }

    public ServerCreateMessage createCoinsMessage(Integer coins) {
        jsonServerProtocol.appendMessage(SharedConstants.INTEGER, coins);
        return this;
    }

    public ServerCreateMessage createPlayersCoinsMessage(Map<User, Integer> playersCoins) {
        jsonServerProtocol.appendMessage(SharedConstants.MAP_PLAYERS_COINS_KEY, playersCoins);
        return this;
    }

    public String buildMessage() {
        String temp = jsonServerProtocol.buildMessage();
        jsonServerProtocol = new JSONProtocol();
        return temp;
    }

}

