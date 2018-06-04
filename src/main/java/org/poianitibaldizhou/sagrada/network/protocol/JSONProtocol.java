package org.poianitibaldizhou.sagrada.network.protocol;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class JSONProtocol {

    private final Map<String, Class> classMap;
    private final JSONObject packet = new JSONObject();

    public JSONProtocol(Map<String, Class> classMap) {
        this.classMap = classMap;
    }

    /**
     * Build a formatted message for communication from server to client.
     * Require a list of key string and then a list of object to send, the method
     * associated tha first key with the first object to send.
     *
     * @param args list of object to send.
     * @param <T>  generic object to send.
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T> void appendMessage(T... args) {
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
                        ((Collection) t).forEach(elem -> list.add(convertToJSON(elem)));
                        jsonObject.put(SharedConstants.TYPE, SharedConstants.COLLECTION);
                        jsonObject.put(SharedConstants.BODY, list);
                        packet.put(key.get(keyPos),jsonObject);
                    } else if (t instanceof Map<?, ?>) {
                        JSONObject main = new JSONObject();
                        JSONObject jsonObject = new JSONObject();
                        ((Map) t).forEach((k, v) -> jsonObject.put(convertToJSON(k).toJSONString(),
                                convertToJSON(v).toJSONString()));
                        main.put(SharedConstants.TYPE, SharedConstants.MAP);
                        main.put(SharedConstants.BODY, jsonObject);
                        packet.put(key.get(keyPos),main);
                    } else
                        packet.put(key.get(keyPos),convertToJSON(t));
                    keyPos++;
                }
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the packet to string
     */
    public String buildMessage() {
        return  packet.toJSONString();
    }

    /**
     * parse a string message for communication from client to server.
     *
     * @param response response from client.
     * @param key for accessing to the correct elem in the body message.
     * @return a correct object (parameter to read).
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
                List<Object> genericList = new ArrayList<>();
                for (Object obj : Objects.requireNonNull(list))
                    genericList.add(convertToObject((JSONObject) obj));
                return genericList;
            }
            if (elem.get(SharedConstants.TYPE).equals(SharedConstants.MAP)) {
                JSONObject map = (JSONObject) elem.get(SharedConstants.BODY);
                Map<Object, Object> genericMap = new HashMap<>();
                for (Object obj : map.entrySet()) {
                    Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) obj;
                    genericMap.put(convertToObject(
                            (JSONObject) jsonParser.parse(entry.getKey().toString())),
                            convertToObject(
                                    (JSONObject) jsonParser.parse(entry.getValue().toString())));
                }
                return genericMap;
            }
            return convertToObject(elem);
        } catch (IllegalArgumentException | ParseException e) {
            throw new ParseException(0);
        }
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
        throw new IllegalArgumentException();
    }

    /**
     * Convert a JSONObject to a correct game object.
     * The method control that to the JSONObject passed corresponds a class
     * JSONable in game and with java reflection it calls the toObject method.
     * <p>
     * The method use a fake constructor for get a object upon which call the invoke.
     *
     * @param jsonObject JSONObject to convert.
     * @return a correct object converted.
     */
    @SuppressWarnings("unchecked")
    public Object convertToObject(JSONObject jsonObject) {
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
