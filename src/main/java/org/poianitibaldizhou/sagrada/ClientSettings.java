package org.poianitibaldizhou.sagrada;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * OVERVIEW: Read client settings from a file
 */
public class ClientSettings {
    private static final String SETTINGS_FILE_PATH = "resources/clientSettings.json";

    private static final String SOCKET_PORT_KEY = "socketPort";
    private static final String RMI_PORT_KEY = "rmiPort";
    private static final String IP_KEY = "ip";

    /**
     * private constructor that hides the public implicit one
     */
    private ClientSettings() {
       // DO NOTHING
    }

    /**
     * Returns the server ip
     * Returns null if problem in reading settings file have occurred.
     *
     * @return server ip
     */
    public static String getIP() {
        return parseString(IP_KEY);
    }

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

    /**
     * Returns the string value associated with the specified key.
     * If error occurs (e.g. key not found in file), returns null
     *
     * @param key key of the string that needs to be read
     * @return value associated with key in settings file. Null if error in reading and parsing the file occurs
     */
    private static String parseString(String key) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String value;

        try {
            jsonObject = (JSONObject) jsonParser.parse(new FileReader(SETTINGS_FILE_PATH));
        } catch (IOException | ParseException e) {
            return null;
        }

        value = (String) jsonObject.get(key);

        return value;
    }


}
