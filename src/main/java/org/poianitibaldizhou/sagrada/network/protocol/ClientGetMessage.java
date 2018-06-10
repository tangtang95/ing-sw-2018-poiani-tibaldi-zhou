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

    /**
     * Get a dice message with key elem.
     *
     * @param message json message from server.
     * @return a dice.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice list from server with key elemList.
     *
     * @param message json message from server.
     * @return a dice list.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice message with key dice.
     *
     * @param message json message from server.
     * @return a dice.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice list from server with key diceList.
     *
     * @param message json message from server.
     * @return a dice list.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a UserWrapper from server with key user.
     *
     * @param message json message from server.
     * @return a UserWrapper.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a UserWrapper from server with key turnUser.
     *
     * @param message json message from server.
     * @return a UserWrapper.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get victory point from server with key victoryPoint.
     *
     * @param message json message from server.
     * @return a vicToryPoint Map<UserWrapper/Point>
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a list of UserWrapper from server with key userList.
     *
     * @param message json message from server.
     * @return a list of UserWrapper.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a game name from server with key gameName.
     *
     * @param message json message from server.
     * @return a game name.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get token of player from server with key token.
     *
     * @param message json message from server.
     * @return player's token.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get timeout from server with key timeout.
     *
     * @param message json message from server.
     * @return time to timeout
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get value from server with key integer.
     *
     * @param message json message from server.
     * @return a value.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get the player's outcome from server with key outcome.
     *
     * @param message json message from server.
     * @return a player's outcome.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice from server with key oldDice.
     *
     * @param message json message from server.
     * @return a dice.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice from server with key newDice.
     *
     * @param message json message from server.
     * @return a dice.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public DiceWrapper getNewDice(String message) throws IOException {
        DiceWrapper diceWrapper;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.NEW_DICE_KEY);
            diceWrapper = DiceWrapper.toObject((JSONObject) jsonObject.get(SharedConstants.BODY));
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return diceWrapper;
    }

    /**
     * Get position from server with key position.
     *
     * @param message json message from server.
     * @return a position.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a list of colors from server with key colorList.
     *
     * @param message json message from server.
     * @return a list of colors.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a dice value from server with key diceValue.
     *
     * @param message json message from server.
     * @return a value.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a list of publicObjectiveCard from server with key publicObjectiveCard.
     *
     * @param message json message from server.
     * @return a list of publicObjectiveCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get TooCard from server with key toolCard.
     *
     * @param message json message from server.
     * @return a toolCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a list of privateObjectiveCard from server with key privateObjectiveCard.
     *
     * @param message json message from server.
     * @return a list of privateObjectiveCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a list of frontBackSchemaCard from server with key frontBackSchemaCard.
     *
     * @param message json message from server.
     * @return a list of frontBackSchemaCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get schemaCard from server with key schemaCard.
     *
     * @param message json message from server.
     * @return a schemaCard.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get color from server with key color.
     *
     * @param message json message from server.
     * @return a color.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a roundTrack from server with key roundTrack.
     *
     * @param message json message rom server.
     * @return a roundTrack.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a map of schemaCard from server with key schemaCardMap.
     *
     * @param message json message from server.
     * @return a map of schemaCard Map<User/SchemaCard>
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Map<UserWrapper,SchemaCardWrapper> getSchemaCards(String message) throws IOException {
        Map<UserWrapper,SchemaCardWrapper> schemaCardWrappers = new HashMap<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message,
                    SharedConstants.MAP_SCHEMA_CARD_KEY);
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

    /**
     * Get a draftPool from server with key draftPool.
     *
     * @param message json message from server.
     * @return a draftPool.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get user from server with key roundUser.
     *
     * @param message json message from server.
     * @return a user.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get player's coins from server with key integer.
     *
     * @param message json message from server.
     * @return a player's coins.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Integer getMyCoins(String message) throws IOException {
        return getValue(message);
    }

    /**
     * Get a map of player's coins from server with key playerCoinsMap.
     *
     * @param message json message from server.
     * @return a map of player's coins Map<User/coins>
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a value from server with key turnValue.
     *
     * @param message json message rom server.
     * @return a value.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
    public Integer getTurnValue(String message) throws IOException {
        Integer turnValue;
        try {
            JSONObject jsonObject = jsonClientProtocol.getResponseByKey(message, SharedConstants.TURN_VALUE_KEY);
            turnValue = Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());
        } catch (ParseException | ClassCastException e) {
            throw new IOException();
        }
        return turnValue;
    }

    /**
     * Get a commandFlow from server with key commandFlow.
     *
     * @param message json message from server.
     * @return a commandFlow.
     * @throws IOException thrown when there is an error in reading message with protocol.
     */
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

    /**
     * Get a ErrorTerminate message from server.
     *
     * @param message json message from server.
     * @return a boolean value: true is the game is finish, false if the game is on.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean hasTerminateGameError(String message) {
        try {
            jsonClientProtocol.getResponseByKey(message, SharedConstants.ERROR_TERMINATE_GAME_KEY).toString();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
