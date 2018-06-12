package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Protocol class for create a json message for networking communication.
 */
public class JSONProtocol {

    /**
     * packet that contains the json message.
     */
    private final JSONObject packet = new JSONObject();

    /**
     * Build a formatted message for communication from server to client.
     * Require a list of key string and then a list of object to send, the method
     * associated tha first key with the first object to send.
     *
     * @param key the key of object.
     * @param t the generic object.
     * @param <T>  generic object to send.
     */
    @SuppressWarnings("unchecked")
    public final <T> void appendMessage(String key, T t) {
        try {
            if (t instanceof Collection<?>) {
                JSONObject jsonObject = new JSONObject();
                JSONArray list = new JSONArray();
                ((Collection) t).forEach(elem -> list.add(convertToJSON(elem)));
                jsonObject.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
                jsonObject.put(SharedConstants.BODY, list);
                packet.put(key, jsonObject);
            } else if (t instanceof Map<?, ?>) {
                JSONObject main = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                ((Map) t).forEach((k, v) -> jsonObject.put(convertToJSON(k).toJSONString(),
                        convertToJSON(v).toJSONString()));
                main.put(SharedConstants.TYPE, SharedConstants.MAP);
                main.put(SharedConstants.BODY, jsonObject);
                packet.put(key, main);
            } else {
                packet.put(key, convertToJSON(t));
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the packet to string
     */
    public String buildMessage() {
        return packet.toJSONString();
    }

    /**
     * parse a string message for communication from client to server.
     *
     * @param response response from client.
     * @param key      for accessing to the correct elem in the body message.
     * @return a correct object (parameter to read).
     * @throws ParseException launch when the message format is wrong.
     */
    @SuppressWarnings("unchecked")
    public JSONObject getResponseByKey(String response, String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();

        JSONObject body = (JSONObject) jsonParser.parse(response);
        return (JSONObject) body.get(key);
    }

    /**
     * Convert a generic to a JSONObject.
     * The method call the toJSON of the object
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
        if (t instanceof  Boolean) {
            jsonObject = new JSONObject();
            jsonObject.put(SharedConstants.TYPE, SharedConstants.BOOLEAN);
            jsonObject.put(SharedConstants.BODY, t.toString());
            return jsonObject;
        }

        throw new IllegalArgumentException();
    }
}
