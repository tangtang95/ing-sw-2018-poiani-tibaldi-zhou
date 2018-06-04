package org.poianitibaldizhou.sagrada.network.protocol;

import org.omg.PortableInterceptor.INACTIVE;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.FrontBackSchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Outcome;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import sun.security.provider.SHA;

import java.util.List;
import java.util.Map;

public class ServerCreateMessage {

    private JSONServerProtocol jsonServerProtocol;

    public ServerCreateMessage() {
        jsonServerProtocol = new JSONServerProtocol();
    }

    public ServerCreateMessage createDiceMessage(Dice dice){
        jsonServerProtocol.appendMessage(SharedConstants.DICE,dice);
        return this;
    }

    public ServerCreateMessage createDiceSwapMessage(Dice oldDice, Dice newDice) {
        jsonServerProtocol.appendMessage(SharedConstants.OLD_DICE, oldDice);
        jsonServerProtocol.appendMessage(SharedConstants.NEW_DICE, newDice);
        return this;
    }

    public ServerCreateMessage createDiceList(List<Dice> diceList) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_LIST_KEY,
                diceList);
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
        jsonServerProtocol = new JSONServerProtocol();
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

    public ServerCreateMessage createOutcomeMessage(Outcome outcome) {
        jsonServerProtocol.appendMessage(SharedConstants.OUTCOME, outcome);
        return this;
    }

    public String buildMessage() {
        String temp = jsonServerProtocol.buildMessage();
        jsonServerProtocol = new JSONServerProtocol();
        return temp;
    }

    public ServerCreateMessage createUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.USER, user);
        return this;
    }

    public ServerCreateMessage createRoundUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.ROUND_USER, user);
        return this;
    }

    public ServerCreateMessage createTurnUserMessage(User user) {
        jsonServerProtocol.appendMessage(SharedConstants.TURN_USER, user);
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
        jsonServerProtocol.appendMessage(SharedConstants.VICTORY_POINT_MAP, map);
        return this;
    }

    public ServerCreateMessage createColorListMessage(List<Color> colors) {
        jsonServerProtocol.appendMessage(SharedConstants.COLOR_LIST_KEY, colors);
        return this;
    }

    public ServerCreateMessage createDiceValueMessage(Integer value) {
        jsonServerProtocol.appendMessage(SharedConstants.DICE_VALUE, value);
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
}
