package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PositionWrapperTest {

    @DataPoint
    public static PositionWrapper positionWrapper;

    @Before
    public void setUp() throws Exception {
        positionWrapper = new PositionWrapper(1,1);
    }

    @Test
    public void toObjectTest() {
        String message = "{\"column\":1,\"row\":1}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(positionWrapper, PositionWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toJsonTest() {
        String message ="{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}";
        assertEquals(message, positionWrapper.toJSON().toJSONString());
    }
}