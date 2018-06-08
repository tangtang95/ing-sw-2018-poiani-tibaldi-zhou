package org.poianitibaldizhou.sagrada.game.model.cards;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PositionTest {

    @DataPoint
    public static Position position;

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"position\",\"body\":{\"column\":1,\"row\":1}}";
        assertEquals(message, position.toJSON().toJSONString());

    }

    @Before
    public void setUp() throws Exception {
        position = new Position(1,1);
    }

    @Test
    public void toObjectTest() {
        String message = "{\"column\":1,\"row\":1}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(position, Position.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}