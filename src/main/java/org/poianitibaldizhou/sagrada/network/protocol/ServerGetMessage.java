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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerGetMessage {
    private JSONProtocol serverNetworkProtocol;

    public ServerGetMessage() {
        serverNetworkProtocol = new JSONProtocol();
    }

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

    public IActionCommand getActionCommand(String message) throws IOException {
        IActionCommand actionCommand;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.ACTION_KEY);
            switch (jsonObject.get(SharedConstants.TYPE).toString()){
                case SharedConstants.USE_TOOL_CARD_ACTION :
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

    public Color getColor(String message) throws IOException {
        Color color;

        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.COLOR);
            color = Color.toObject(jsonObject);
        } catch(ParseException | ClassCastException e) {
            throw new IOException();
        }

        return color;
    }

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

    public String getErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.GET_ERROR);
        return JSONObject.toJSONString(error);
    }

    public String reconnectErrorMessage() {
        Map<String, String> error = new HashMap<>();
        error.putIfAbsent(SharedConstants.GET_ERROR_KEY, SharedConstants.RECONNECT_ERROR);
        return JSONObject.toJSONString(error);
    }

    public boolean getBoolean(String message) throws IOException {
        Boolean b;
        try {
            JSONObject jsonObject = serverNetworkProtocol.getResponseByKey(message, SharedConstants.USER_NAME_STRING_KEY);
            b = Boolean.valueOf(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return b;
    }
}
