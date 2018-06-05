package org.poianitibaldizhou.sagrada.network.protocol;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.IActionCommand;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerGetMessage {
    private JSONServerProtocol serverNetworkProtocol;

    public ServerGetMessage() {
        serverNetworkProtocol = new JSONServerProtocol();
    }

    public String getToken(String message) throws IOException {
        String token;

        try {
            token = (String) serverNetworkProtocol.getResponseByKey(message, SharedConstants.TOKEN_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return token;
    }

    public String getGameName(String message) throws IOException {
        String gameName;
        try {
            gameName = (String) serverNetworkProtocol.getResponseByKey(message, SharedConstants.GAME_NAME_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return gameName;
    }

    public SchemaCard getSchemaCard(String message) throws IOException {
        SchemaCard schemaCard;
        try {
            schemaCard = (SchemaCard) serverNetworkProtocol.getResponseByKey(message, SharedConstants.SCHEMA_CARD);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return schemaCard;
    }

    public User getUser(String message) throws IOException {
        User user;

        try {
            user = (User) serverNetworkProtocol.getResponseByKey(message, SharedConstants.USER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return user;
    }

    public Position getPosition(String message) throws IOException {
        Position position;

        try {
            position = (Position) serverNetworkProtocol.getResponseByKey(message, SharedConstants.POSITION);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return position;
    }

    public Dice getDice(String message) throws IOException {
        Dice dice;

        try {
            dice = (Dice) serverNetworkProtocol.getResponseByKey(message, SharedConstants.DICE);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return dice;
    }

    public ToolCard getToolCard(String message) throws IOException {
        ToolCard toolCard;

        try {
            toolCard = (ToolCard) serverNetworkProtocol.getResponseByKey(message, SharedConstants.TOOL_CARD);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return toolCard;
    }

    public IActionCommand getActionCommand(String message) throws IOException {
        IActionCommand actionCommand;
        try {
            actionCommand = (IActionCommand) serverNetworkProtocol.getResponseByKey(message, SharedConstants.ACTION_KEY);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return actionCommand;
    }

    public PrivateObjectiveCard getPrivateObjectiveCard(String message) throws IOException {
        PrivateObjectiveCard privateObjectiveCard;

        try {
            privateObjectiveCard = (PrivateObjectiveCard) serverNetworkProtocol.getResponseByKey(message, SharedConstants.PRIVATE_OBJECTIVE_CARD);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return privateObjectiveCard;
    }

    public Integer getInteger(String message) throws IOException {
        Integer value;

        try {
            value = (Integer) serverNetworkProtocol.getResponseByKey(message, SharedConstants.INTEGER);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }

        return value;
    }

    public Color getColor(String message) throws IOException {
        Color color;

        try {
            color = (Color) serverNetworkProtocol.getResponseByKey(message, SharedConstants.COLOR);
        } catch(ParseException | ClassCastException e) {
            throw new IOException();
        }

        return color;
    }

    public String getUserName(String message) throws IOException {
        String username;
        try {
            username = (String) serverNetworkProtocol.getResponseByKey(message, SharedConstants.USER_NAME_STRING);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return username;
    }

    public Boolean getBoolean(String message) throws IOException {
        Boolean bool;

        try {
            bool = (Boolean) serverNetworkProtocol.getResponseByKey(message, SharedConstants.BOOLEAN);
        } catch(ParseException | ClassCastException e) {
            throw new IOException();
        }

        return bool;
    }

    public String getErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.GET_ERROR);
        //TODO RICCARDO
        return null;
    }
}
