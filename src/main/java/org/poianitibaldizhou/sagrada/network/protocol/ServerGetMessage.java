package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.UseCardAction;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for getting a message from client.
 */
public class ServerGetMessage {

    /**
     * network protocol.
     */
    private JSONProtocol serverNetworkProtocol;

    /**
     * ServerGetMessage constructor.
     */
    public ServerGetMessage() {
        serverNetworkProtocol = new JSONProtocol();
    }

    /**
     * Get a player's token from client with key token.
     *
     * @param message json message from client.
     * @return a player's token.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public String getToken(String message) throws IOException {
        String token;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.TOKEN_KEY);
            token = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
            throw new IOException();
        }
        return token;
    }

    /**
     * Get a game name from client with key gameName.
     *
     * @param message json message from client.
     * @return a game name.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public String getGameName(String message) throws IOException {
        String gameName;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.GAME_NAME_KEY);
            gameName = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return gameName;
    }

    /**
     * Get schemaCard from client with key schemaCard.
     *
     * @param message json message from client.
     * @return a schemaCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public SchemaCard getSchemaCard(String message) throws IOException {
        SchemaCard schemaCard;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.SCHEMA_CARD);
            schemaCard = SchemaCard.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return schemaCard;
    }

    /**
     * Get a user from client with key user.
     *
     * @param message json message from client.
     * @return a user.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public User getUser(String message) throws IOException {
        User user;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.USER);
            user = User.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return user;
    }

    /**
     * Get a position from client with key position.
     *
     * @param message json message fro client.
     * @return a position.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Position getPosition(String message) throws IOException {
        Position position;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.POSITION);
            position = Position.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException | IllegalArgumentException e) {
            throw new IOException();
        }

        return position;
    }

    /**
     * Get dice rom client with key dice.
     *
     * @param message json message from client.
     * @return a dice.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Dice getDice(String message) throws IOException {
        Dice dice;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.DICE);
            dice = Dice.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return dice;
    }

    /**
     * Get toolCard from client with key toolCard.
     *
     * @param message json message from client.
     * @return a toolCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public ToolCard getToolCard(String message) throws IOException {
        ToolCard toolCard;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.TOOL_CARD);
            toolCard = ToolCard.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return toolCard;
    }

    /**
     * Get a acton command from client with key action
     *
     * @param message json message from client.
     * @return an action command.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public IActionCommand getActionCommand(String message) throws IOException {
        IActionCommand actionCommand;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.ACTION_KEY);
            switch (jsonObject.get(SharedConstants.TYPE).toString()) {
                case SharedConstants.USE_TOOL_CARD_ACTION:
                    actionCommand = new UseCardAction();
                    break;
                case SharedConstants.PLACE_DICE_ACTION:
                    actionCommand = new PlaceDiceAction();
                    break;
                default:
                    actionCommand = new EndTurnAction();
            }
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return actionCommand;
    }

    /**
     * Get a privateObjectiveCard from client with key privateObjectiveCard.
     *
     * @param message json message from client.
     * @return a privateObjectiveCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public PrivateObjectiveCard getPrivateObjectiveCard(String message) throws IOException {
        PrivateObjectiveCard privateObjectiveCard;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message,
                    SharedConstants.PRIVATE_OBJECTIVE_CARD);
            privateObjectiveCard = PrivateObjectiveCard.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return privateObjectiveCard;
    }

    /**
     * Get integer value from client with key integer.
     *
     * @param message json message from client
     * @return a integer.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Integer getInteger(String message) throws IOException {
        Integer value;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.INTEGER);
            value = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return value;
    }

    /**
     * Get a color from client with key color.
     *
     * @param message json message from client.
     * @return a color.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Color getColor(String message) throws IOException {
        Color color;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.COLOR);
            color = Color.toObject(jsonObject);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return color;
    }

    /**
     * Get player's username from client with key username.
     *
     * @param message json message from client.
     * @return a player's username.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public String getUserName(String message) throws IOException {
        String username;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.USER_NAME_STRING_KEY);
            username = (String) jsonObject.get(SharedConstants.BODY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return username;
    }

    /**
     * Get a bool value from client with key boolean.
     *
     * @param message json message from client.
     * @return a bool value.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public boolean getBoolean(String message) throws IOException {
        Boolean b;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.BOOLEAN);
            b = Boolean.valueOf(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return b;
    }
}
