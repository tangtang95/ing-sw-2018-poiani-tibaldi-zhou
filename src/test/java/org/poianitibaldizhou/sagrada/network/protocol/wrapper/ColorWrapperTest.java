package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class ColorWrapperTest {

    @DataPoint
    public static ColorWrapper colorWrapper;

    @Test
    public void toJsonTest() {
        String message = colorWrapper.toJSON().toJSONString();
        String test = "{\"type\":\"color\",\"body\":\"GREEN\"}";
        assertEquals(message,test);
    }

    @Test
    public void toObjectTest() {
        JSONParser jsonParser = new JSONParser();
        String test = "{\"type\":\"color\",\"body\":\"GREEN\"}";
        try {
            assertEquals(colorWrapper,ColorWrapper.toObject((JSONObject) jsonParser.parse(test)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Before
    public void setUp() throws Exception {
        colorWrapper = ColorWrapper.GREEN;
    }
}