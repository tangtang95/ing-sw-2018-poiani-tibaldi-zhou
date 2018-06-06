package org.poianitibaldizhou.sagrada;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Settings {

    private static final String SETTINGS_FILE_PATH = "resources/settings.json";
    private static final String LOBBY_TIMEOUT_KEY = "lobbyTimeout";
    private static final String PLAYER_TIMEOUT_KEY = "playerTimeout";
    private static final String SOCKET_PORT_KEY = "socketPort";
    private static final String RMI_PORT_KEY = "rmiPort";

    /**
     * Returns the socket port on which the server listens.
     * Returns null if problem in reading settings file have occurred.
     *
     * @return socket port
     */
    public static Integer getSocketPort() {
        return parseInteger(SOCKET_PORT_KEY);
    }

    /**
     * Returns the RMI port on which the server listens.
     * Returns null if problem in reading settings file have occurred.
     *
     * @return RMI port
     */
    public static Integer getRMIPort() {
        return parseInteger(RMI_PORT_KEY);
    }

    /**
     * Returns the duration of the timeout before that the game starts from a lobby without being full.
     * Returns null if problem in reading settings file have occurred.
     *
     * @return timeout duration in millis
     */
    public static Long getLobbyTimeout() {
        return parseDouble(LOBBY_TIMEOUT_KEY);
    }

    /**
     * Returns the duration of the timeout for the player turn.
     * Returns null if problem in reading settings file have occurred.
     *
     * @return timeout duration in millis
     */
    public static Long getPlayerTimeout() {
        return parseDouble(PLAYER_TIMEOUT_KEY);
    }


    /**
     * Returns the double value associated with the specified key.
     * If error occurs (e.g. key not found in file), returns null
     *
     * @param key key of the double that needs to be read
     * @return value associated with key in settings file. Null if error in reading and parsing the file occurs
     */
    private static Long parseDouble(String key) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        Long value;

        try {
            jsonObject = (JSONObject) jsonParser.parse(new FileReader(SETTINGS_FILE_PATH));
        } catch (IOException | ParseException e) {
            return null;
        }

        value = (Long) jsonObject.get(key);

        return value;
    }

    /**
     * Returns the integer value associated with the specified key.
     * If error occurs (e.g. key not found in file), returns null
     *
     * @param key key of the integer that needs to be read
     * @return value associated with key in settings file. Null if error in reading and parsing the file occurs
     */
    private static Integer parseInteger(String key) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        Integer value;

        try {
            jsonObject = (JSONObject) jsonParser.parse(new FileReader(SETTINGS_FILE_PATH));
        } catch (IOException | ParseException e) {
            return null;
        }

        value = (Integer) jsonObject.get(key);

        return value;
    }
}
