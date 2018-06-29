package org.poianitibaldizhou.sagrada.lobby.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

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
        user = new User("test", NetworkUtility.encryptUsername("test"));
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
        User user = new User("name", NetworkUtility.encryptUsername("name"));

        assertEquals(user, new User("name", NetworkUtility.encryptUsername("name")));
        assertNotEquals(user, new User("name", "name"));
        assertNotEquals(user, new User("notEqualsName", NetworkUtility.encryptUsername("name")));
        assertNotEquals(user, null);
    }

    @Test
    public void testHashCode() {
        User user = new User("name", NetworkUtility.encryptUsername("name"));

        assertEquals(user.hashCode(), new User("name", NetworkUtility.encryptUsername("name")).hashCode());
        assertNotEquals(user.hashCode(), new User("name", "name").hashCode());
        assertNotEquals(user.hashCode(), new User("notEqualsName", NetworkUtility.encryptUsername("name")).hashCode());
    }

    @Test
    public void testToString() {
        String message = "Username: username ;token";

        assertEquals(message, new User("username", "token").toString());
    }
}