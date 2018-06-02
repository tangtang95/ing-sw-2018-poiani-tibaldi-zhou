package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Class to convert to string the object for communicating with server.
 */
public class ClientNetworkProtocol {

    /**
     * Build a formatted message for communication from client to server.
     * Require a list of key string and then a list of object to send, the method
     * associated tha first key with the first object to send.
     *
     * @param args list of object to send.
     * @param <T>  generic object to send.
     * @return formatted string or null if it fail.
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> String createMessage(T... args) {
        JSONObject packet = new JSONObject();
        List<String> key = new ArrayList<>();
        int pos = 0;
        int keyPos = 0;

        try {
            for (T t : args) {
                if (pos < args.length / 2) {
                    key.add((String) t);
                    pos++;
                }
                else {
                    if (t instanceof Collection<?>) {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray list = new JSONArray();
                        list.addAll(((Collection) t));
                        jsonObject.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
                        jsonObject.put(SharedConstants.BODY, list);
                        packet.put(key.get(keyPos),jsonObject);
                    } else if (t instanceof Map<?, ?>) {
                        JSONObject main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        ((Map) t).forEach(jsonObject::put);
                        main.put(SharedConstants.TYPE, SharedConstants.MAP);
                        main.put(SharedConstants.BODY, jsonObject);
                        packet.put(key.get(keyPos),main);
                    } else
                        packet.put(key.get(keyPos),t);
                    keyPos++;
                }
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return null;
        }
        return packet.toJSONString();
    }

    /**
     * parse a string message for communication from server to client.
     *
     * @param response response from server.
     * @param key for accessing to the correct elem in the body message.
     * @return a correct JSONObject (parameter to read).
     * @throws ParseException launch when the message format is wrong.
     */
    @SuppressWarnings("unchecked")
    public Object getResponseByKey(String response, String key) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject body = (JSONObject) jsonParser.parse(response);
        JSONObject elem = (JSONObject) body.get(key);
        try {
            if (elem.get(SharedConstants.TYPE).equals(SharedConstants.COLLECTION)) {
                JSONArray list = (JSONArray) elem.get(SharedConstants.BODY);
                List<JSONObject> genericList = new ArrayList<>();
                for (Object obj : Objects.requireNonNull(list))
                    genericList.add((JSONObject) obj);
                return genericList;
            }
            if (elem.get(SharedConstants.TYPE).equals(SharedConstants.MAP)) {
                JSONObject map = (JSONObject) elem.get(SharedConstants.BODY);
                Map<JSONObject, JSONObject> genericMap = new HashMap<>();
                for (Object obj : map.entrySet()) {
                    Map.Entry<JSONObject, JSONObject> entry = (Map.Entry<JSONObject, JSONObject>) obj;
                    genericMap.put((JSONObject) jsonParser.parse(entry.getKey().toString()),
                            (JSONObject) jsonParser.parse(entry.getValue().toString()));
                }
                return genericMap;
            }
            return elem;
        } catch (IllegalArgumentException | ParseException e) {
            throw new ParseException(0);
        }
    }
}
