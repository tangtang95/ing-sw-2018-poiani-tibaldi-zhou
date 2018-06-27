package org.poianitibaldizhou.sagrada.network.protocol;

import org.jetbrains.annotations.NotNull;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

/**
 * Class for creating a message to send to server.
 */
public class ClientCreateMessage {

    /**
     * network protocol.
     */
    private JSONProtocol jsonClientProtocol;

    /**
     * ClientCreateMessage constructor.
     */
    public ClientCreateMessage() {
        jsonClientProtocol = new JSONProtocol();
    }

    /**
     * Create a gameName message.
     *
     * @param gameName the game name to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createGameNameMessage(@NotNull String gameName) {
        jsonClientProtocol.appendMessage(SharedConstants.GAME_NAME_KEY, gameName);
        return this;
    }

    /**
     * Create a token message.
     *
     * @param token the token of player to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createTokenMessage(@NotNull String token) {
        jsonClientProtocol.appendMessage(SharedConstants.TOKEN_KEY, token);
        return this;
    }

    /**
     * Create a schemaCard message.
     *
     * @param schemaCard the schemaCard to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createSchemaCardMessage(@NotNull SchemaCardWrapper schemaCard) {
        jsonClientProtocol.appendMessage(SharedConstants.SCHEMA_CARD, schemaCard);
        return this;
    }

    /**
     * Create a username message.
     *
     * @param username the username of player to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createUsernameMessage(@NotNull String username) {
        jsonClientProtocol.appendMessage(SharedConstants.USER_NAME_STRING_KEY, username);
        return this;
    }

    /**
     * Create a value message.
     *
     * @param value the value to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createValueMessage(@NotNull Integer value) {
        jsonClientProtocol.appendMessage(SharedConstants.INTEGER, value);
        return this;
    }

    /**
     * Create a dice message.
     *
     * @param dice the dice to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createDiceMessage(@NotNull DiceWrapper dice) {
        jsonClientProtocol.appendMessage(SharedConstants.DICE, dice);
        return this;
    }

    /**
     * Create a color message.
     *
     * @param color the color to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createColorMessage(@NotNull ColorWrapper color) {
        jsonClientProtocol.appendMessage(SharedConstants.COLOR, color);
        return this;
    }

    /**
     * Create a boolean message.
     *
     * @param bool the bool value to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createBooleanMessage(@NotNull Boolean bool) {
        jsonClientProtocol.appendMessage(SharedConstants.BOOLEAN, bool);
        return this;
    }

    /**
     * Create a position message.
     *
     * @param positionWrapper the position to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createPositionMessage(@NotNull PositionWrapper positionWrapper) {
        jsonClientProtocol.appendMessage(SharedConstants.POSITION, positionWrapper);
        return this;
    }

    /**
     * Creates a message containing a private objective card
     *
     * @param privateObjectiveCardWrapper private objective card that needs to be sent
     * @return the ClientCreateMessage with the message added to the packed
     */
    public ClientCreateMessage createPrivateObjectiveCardMessage(@NotNull PrivateObjectiveCardWrapper privateObjectiveCardWrapper) {
        jsonClientProtocol.appendMessage(SharedConstants.PRIVATE_OBJECTIVE_CARD, privateObjectiveCardWrapper);
        return this;
    }

    /**
     * Create a toolCard message.
     *
     * @param toolCardWrapper the toolCard to send to server.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createToolCardMessage(@NotNull ToolCardWrapper toolCardWrapper) {
        jsonClientProtocol.appendMessage(SharedConstants.TOOL_CARD, toolCardWrapper);
        return this;
    }

    /**
     * Create a action message.
     *
     * @param iActionWrapper the action that you will do.
     * @return the ClientCreateMessage with the message added to the packet.
     */
    public ClientCreateMessage createActionMessage(@NotNull IActionWrapper iActionWrapper) {
        jsonClientProtocol.appendMessage(SharedConstants.ACTION_KEY, iActionWrapper);
        return this;
    }

    /**
     * Make the current packet and it creates a new empty packet.
     *
     * @return the full packet to string for sending.
     */
    public String buildMessage() {
        String temp = jsonClientProtocol.buildMessage();
        jsonClientProtocol = new JSONProtocol();
        return temp;
    }
}
