package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for getting a message from server.
 */
public class ClientGetMessage {

    /**
     * network protocol.
     */
    private JSONProtocol jsonClientProtocol;

    /**
     * ClientGetMessage constructor.
     */
    public ClientGetMessage() {
        jsonClientProtocol = new JSONProtocol();
    }

    public DiceWrapper getDiceElem(String message) throws IOException {
        DiceWrapper diceWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.ELEM);
            diceWrapper = DiceWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    public List<DiceWrapper> getDiceElemList(String message) throws IOException {
        List<DiceWrapper> diceWrapperList = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.ELEM_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                diceWrapperList.add(DiceWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapperList;
    }

    public DiceWrapper getDice(String message) throws IOException {
        DiceWrapper diceWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE);
            diceWrapper = DiceWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    public List<DiceWrapper> getDiceList(String message) throws IOException {
        List<DiceWrapper> diceWrapper = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                diceWrapper.add(DiceWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    public UserWrapper getUserWrapper(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.USER);
            userWrapper = UserWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrapper;
    }

    public UserWrapper getTurnUserWrapper(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.TURN_USER_KEY);
            userWrapper = UserWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrapper;
    }

    public Map<UserWrapper, Integer> getVictoryPoint(String message) throws IOException {
        Map<UserWrapper, Integer> victoryPoint = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.VICTORY_POINT_MAP_KEY);
            JSONObject map = (JSONObject) jsonObject.get(SharedConstants.BODY);
            for (Object o : map.keySet()) {
                JSONObject user = (JSONObject) jsonParser.parse(o.toString());
                JSONObject val = (JSONObject) jsonParser.parse(map.get(o).toString());
                victoryPoint.put(UserWrapper.toObject((JSONObject) user.get(SharedConstants.BODY)),
                        Integer.parseInt(val.get(SharedConstants.BODY).toString()));
            }
        } catch (ParseException e) {
            throw new IOException();
        }
        return victoryPoint;
    }


    public List<UserWrapper> getListOfUserWrapper(String message) throws IOException {
        List<UserWrapper> userWrappers = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.USER_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                userWrappers.add(UserWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrappers;
    }

    public String getGameName(String message) throws IOException {
        String gameName;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,SharedConstants.GAME_NAME_KEY);
            gameName = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return gameName;
    }

    public String getToken(String message) throws IOException {
        String token;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,SharedConstants.TOKEN_KEY );
            token = jsonObject.get(SharedConstants.BODY).toString();
        } catch(ParseException | ClassCastException e) {
            throw new IOException();
        }
        return token;
    }

    public String getTimeout(String message) throws IOException {
        String timeout;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.TIMEOUT_KEY);
            timeout = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return timeout;
    }

    public Integer getValue(String message) throws IOException {
        Integer value;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.INTEGER);
            value = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return value;
    }

    public String getOutcome(String message) throws IOException {
        String outcome;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.OUTCOME);
            outcome = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return outcome;
    }

    public DiceWrapper getOldDice(String message) throws IOException {
        DiceWrapper diceWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.OLD_DICE_KEY);
            diceWrapper = DiceWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    public DiceWrapper getNewDice (String message) throws IOException {
        DiceWrapper diceWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.NEW_DICE_KEY);
            diceWrapper = DiceWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    public PositionWrapper getPosition(String message) throws IOException {
        PositionWrapper positionWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.POSITION);
            positionWrapper = PositionWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return positionWrapper;
    }

    public List<ColorWrapper> getColorList(String message) throws IOException {
        List<ColorWrapper> colorWrappers = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.COLOR_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                colorWrappers.add(ColorWrapper.toObject((JSONObject) o));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return colorWrappers;
    }

    public Integer getDiceValue(String message) throws IOException {
        Integer value;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.DICE_VALUE_KEY);
            value = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return value;
    }

    public List<PublicObjectiveCardWrapper> getPublicObjectiveCards(String message) throws IOException {
        List<PublicObjectiveCardWrapper> poc = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.PUBLIC_OBJECTIVE_CARD_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                poc.add(PublicObjectiveCardWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return poc;
    }

    public List<ToolCardWrapper> getToolCards(String message) throws IOException {
        List<ToolCardWrapper> toolCardWrappers = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.TOOL_CARD_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                toolCardWrappers.add(ToolCardWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return toolCardWrappers;
    }

    public List<PrivateObjectiveCardWrapper> getPrivateObjectiveCards(String message) throws IOException {
        List<PrivateObjectiveCardWrapper> poc = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.PRIVATE_OBJECTIVE_CARD_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                poc.add(PrivateObjectiveCardWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return poc;
    }

    public List<FrontBackSchemaCardWrapper> getFrontBackSchemaCards(String message) throws IOException {
        List<FrontBackSchemaCardWrapper> fbsc = new ArrayList<>();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.FRONT_CARD_LIST_KEY);
            JSONArray jsonArray = (JSONArray) jsonObject.get(SharedConstants.BODY);
            for (Object o : jsonArray)
                fbsc.add(FrontBackSchemaCardWrapper.toObject((JSONObject)((JSONObject) o).get(SharedConstants.BODY)));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return fbsc;
    }

    public SchemaCardWrapper getSchemaCard(String message) throws IOException {
        SchemaCardWrapper schemaCardWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.SCHEMA_CARD);
            schemaCardWrapper = SchemaCardWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return schemaCardWrapper;
    }

    public ColorWrapper getColor(String message) throws IOException{
        ColorWrapper colorWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.COLOR);
            colorWrapper = ColorWrapper.toObject(jsonObject);
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return colorWrapper;
    }

    public RoundTrackWrapper getRoundTrack(String message) throws IOException {
        RoundTrackWrapper roundTrack;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.ROUND_TRACK);
            roundTrack = RoundTrackWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return roundTrack;
    }


    public Map<UserWrapper,SchemaCardWrapper> getSchemaCards(String message) throws IOException {
        Map<UserWrapper,SchemaCardWrapper> schemaCardWrappers = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.MAP_SCHEMA_CARD_KEY);
            if(jsonObject == null){
                jsonObject.get(SharedConstants.BODY);
            }
            JSONObject map = (JSONObject) jsonObject.get(SharedConstants.BODY);

            for (Object o : map.keySet()) {
                JSONObject user = (JSONObject) jsonParser.parse(o.toString());
                JSONObject schema = (JSONObject) jsonParser.parse(map.get(o).toString());
                schemaCardWrappers.put(UserWrapper.toObject((JSONObject) user.get(SharedConstants.BODY)),
                        SchemaCardWrapper.toObject((JSONObject) schema.get(SharedConstants.BODY)));
            }
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return schemaCardWrappers;
    }

    public DraftPoolWrapper getDraftPool(String message) throws IOException {
        DraftPoolWrapper draftPoolWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.DRAFT_POOL);
            draftPoolWrapper = DraftPoolWrapper.toObject((JSONObject)jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return draftPoolWrapper;
    }

    public UserWrapper getRoundUser(String message) throws IOException {
        UserWrapper userWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.ROUND_USER_KEY);
            userWrapper = UserWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return userWrapper;
    }

    public Integer getMyCoins(String message) throws IOException {
        Integer value;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.INTEGER);
            value = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return value;
    }

    public Map<UserWrapper,Integer> getPlayersCoins(String message) throws IOException {
        Map<UserWrapper,Integer> playersCoins = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.MAP_PLAYERS_COINS_KEY);
            JSONObject map = (JSONObject) jsonObject.get(SharedConstants.BODY);
            for (Object o : map.keySet()) {
                JSONObject user = (JSONObject) jsonParser.parse(o.toString());
                Integer value = Integer.parseInt(
                        ((JSONObject)jsonParser.parse(map.get(o).toString())).get(SharedConstants.BODY).toString());
                playersCoins.put(UserWrapper.toObject((JSONObject) user.get(SharedConstants.BODY)),
                        value);
            }
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return playersCoins;
    }

    public int getTurnValue(String message) throws IOException {
        Integer turnValue;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.TURN_VALUE_KEY);
            turnValue = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return turnValue;
    }


    public String getCommandFlow(String message) throws IOException {
        String command;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.COMMAND_FLOW);
            command = jsonObject.get(SharedConstants.BODY).toString();
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return command;
    }


    public boolean hasTerminateGameError(String response) {
        String message = null;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.ERROR_TERMINATE_GAME_KEY);
            message = jsonObject.get(SharedConstants.BODY).toString();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
