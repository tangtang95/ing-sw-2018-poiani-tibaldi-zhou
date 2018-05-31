package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class to convert to string the object for communicating with clients.
 */
public class ServerNetworkProtocol {

    private final Map<String, Class> classMap = new HashMap<>();

    /**
     * constructor.
     * Set up the classMap with the loadable class of the game.
     */
    public ServerNetworkProtocol() {
        classMap.put(SharedConstants.COLLECTION, Collection.class);
        classMap.put(SharedConstants.DICE, Dice.class);
        classMap.put(SharedConstants.DRAFT_POOL, DraftPool.class);
        classMap.put(SharedConstants.INTEGER, Integer.class);
        classMap.put(SharedConstants.MAP, Map.class);
        classMap.put(SharedConstants.PLAYER, Player.class);
        classMap.put(SharedConstants.POSITION, Position.class);
        classMap.put(SharedConstants.PRIVATE_OBJECTIVE_CARD, PrivateObjectiveCard.class);
        classMap.put(SharedConstants.PUBLIC_OBJECTIVE_CARD, PublicObjectiveCard.class);
        classMap.put(SharedConstants.ROUND_TRACK, RoundTrack.class);
        classMap.put(SharedConstants.STRING, String.class);
        classMap.put(SharedConstants.TOOL_CARD, ToolCard.class);
        classMap.put(SharedConstants.USER, User.class);
    }

    /**
     * build a formatted message for communication from server to client.
     *
     * @param args list of object to send.
     * @param <T>  generic object to send.
     * @return formatted string.
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> String createMessage(T... args) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (T t : args) {
                if (t instanceof Collection<?>) {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray list = new JSONArray();
                    ((Collection) t).forEach(elem -> list.add(convertToJSON(elem)));
                    jsonObject.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
                    jsonObject.put(SharedConstants.BODY, list);
                    jsonArray.add(jsonObject);
                } else if (t instanceof Map<?, ?>) {
                    JSONObject main = new JSONObject();
                    JSONObject jsonObject = new JSONObject();
                    ((Map) t).forEach((k, v) -> jsonObject.put(convertToJSON(k).toJSONString(),
                            convertToJSON(v).toJSONString()));
                    main.put(SharedConstants.TYPE, SharedConstants.MAP);
                    main.put(SharedConstants.BODY, jsonObject);
                    jsonArray.add(main);
                } else
                    jsonArray.add(convertToJSON(t));
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return jsonArray.toJSONString();
    }


    /**
     * parse a string message for communication from clint to server.
     *
     * @param response response from client.
     * @return list of object (parameter to read).
     * @throws ParseException launch when the message format is wrong.
     */
    @SuppressWarnings("unchecked")
    public List<Object> parsingResponse(String response) throws ParseException {
        List<Object> param = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(response);
        try {
            for (Object object : Objects.requireNonNull(jsonArray)) {
                JSONObject elem = (JSONObject) object;
                if (elem.get(SharedConstants.TYPE).equals(SharedConstants.COLLECTION)) {
                    JSONArray list = (JSONArray) elem.get(SharedConstants.BODY);
                    List<Object> genericList = new ArrayList<>();
                    for (Object obj : Objects.requireNonNull(list))
                        genericList.add(convertToObject((JSONObject) obj));
                    param.add(genericList);
                } else if (elem.get(SharedConstants.TYPE).equals(SharedConstants.MAP)) {
                    JSONObject map = (JSONObject) elem.get(SharedConstants.BODY);
                    Map<Object, Object> genericMap = new HashMap<>();
                    for (Object obj : map.entrySet()) {
                        Map.Entry<Object,Object> entry = (Map.Entry<Object, Object>) obj;
                        genericMap.put(convertToObject(
                                (JSONObject) jsonParser.parse(entry.getKey().toString())),
                                convertToObject(
                                        (JSONObject) jsonParser.parse(entry.getValue().toString())));
                    }
                    param.add(genericMap);
                } else
                    param.add(convertToObject(elem));
            }
        } catch (IllegalArgumentException | ParseException e) {
            throw new ParseException(0);
        }
        return param;
    }

    /**
     * Convert a generic to a JSONObject.
     *
     * @param t   object to convert a JSONObject.
     * @param <T> generic object to convert.
     * @return JSONObject.
     */
    @SuppressWarnings("unchecked")
    private <T> JSONObject convertToJSON(T t) {
        JSONObject jsonObject;
        if (t instanceof JSONable)
            return ((JSONable) t).toJSON();

        if (t instanceof String) {
            jsonObject = new JSONObject();
            jsonObject.put(SharedConstants.TYPE, SharedConstants.STRING);
            jsonObject.put(SharedConstants.BODY, t.toString());
            return jsonObject;
        }
        if (t instanceof Integer) {
            jsonObject = new JSONObject();
            jsonObject.put(SharedConstants.TYPE, SharedConstants.INTEGER);
            jsonObject.put(SharedConstants.BODY, t.toString());
            return jsonObject;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Convert a JSONObject to a correct game object.
     *
     * @param jsonObject JSONObject to convert.
     * @return a correct object converted.
     */
    @SuppressWarnings("unchecked")
    private Object convertToObject(JSONObject jsonObject) {
        String className = (String) jsonObject.get(SharedConstants.TYPE);

        if (className.equals(SharedConstants.STRING))
            return jsonObject.get(SharedConstants.BODY).toString();
        if (className.equals(SharedConstants.INTEGER))
            return Integer.parseInt(jsonObject.get(SharedConstants.BODY).toString());

        Class[] interfaces = classMap.get(className).getInterfaces();
        Boolean isConvertible = false;

        for (Class c : interfaces)
            if (c.isAssignableFrom(JSONable.class.getClass()))
                isConvertible = true;
        if (isConvertible) {
            try {
                Method method = classMap.get(className).getDeclaredMethod("toObject", JSONObject.class);
                Constructor fakeConstructor = null;
                for (Constructor constructor : classMap.get(className).getDeclaredConstructors())
                    if (constructor.getParameterCount() == 0) {
                        constructor.setAccessible(true);
                        fakeConstructor = constructor;
                    }
                if (fakeConstructor != null)
                    return method.invoke(fakeConstructor.newInstance(),
                            (JSONObject) jsonObject.get(SharedConstants.BODY));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
                    | InstantiationException e) {
                throw new IllegalArgumentException();
            }
        }
        throw new IllegalArgumentException();
    }
}













