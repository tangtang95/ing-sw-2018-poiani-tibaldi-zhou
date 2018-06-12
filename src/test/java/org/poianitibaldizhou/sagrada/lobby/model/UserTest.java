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
        user = new User("test", String.valueOf("test".hashCode()));
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

    @Test
    public void equalsTest() {
        User user = new User("name", String.valueOf("name".hashCode()));

        assertFalse(user.equals(null));
        assertEquals(user, new User("name", String.valueOf("name".hashCode())));
        assertNotEquals(user, new User("name", "name"));
        assertNotEquals(user, new User("notEqualsName", String.valueOf("name".hashCode())));
    }

    @Test
    public void testHashCode() {
        User user = new User("name", String.valueOf("name".hashCode()));

        assertEquals(user.hashCode(), new User("name", String.valueOf("name".hashCode())).hashCode());
        assertNotEquals(user.hashCode(), new User("name", "name").hashCode());
        assertNotEquals(user.hashCode(), new User("notEqualsName", String.valueOf("name".hashCode())).hashCode());
    }

    @Test
    public void testToString() {
        String message = "Username: username ;token";

        assertEquals(message, new User("username", "token").toString());
    }
}