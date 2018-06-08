package org.poianitibaldizhou.sagrada.game.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class ColorTest {

    @DataPoint
    public static Color color;

    @Test
    public void toJsonTest() {
        String message = color.toJSON().toJSONString();
        String test = "{\"type\":\"color\",\"body\":\"BLUE\"}";
        assertEquals(message,test);
    }

    @Before
    public void setUp() throws Exception {
        color = Color.BLUE;
    }

    @Test
    public void toObjectTest() {
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        String test = "{\"type\":\"color\",\"body\":\"BLUE\"}";
        try {
            assertEquals(color,Color.toObject((JSONObject) jsonParser.parse(test)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}