package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class UserWrapperTest {

    @DataPoint
    public static UserWrapper userWrapper;

    @Before
    public void setUp() throws Exception {
        userWrapper = new UserWrapper("test");
    }

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"user\",\"body\":{\"userName\":\"test\"}}";
        assertEquals(message, userWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        String message = "{\"userName\":\"test\"}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(userWrapper, UserWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}