package org.poianitibaldizhou.sagrada.lobby.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class UserTest {

    @DataPoint
    public static User user;

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}";
        assertEquals(message, user.toJSON().toJSONString());
    }

    @Before
    public void setUp() throws Exception {
        user = new User("test", "123456789");
    }

    @Test
    public void toObjectTest() {
        String message = "{\"userName\":\"test\"}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(user, User.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}