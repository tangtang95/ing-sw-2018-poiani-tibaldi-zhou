package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class DiceWrapperTest {

    @DataPoint
    public static DiceWrapper diceWrapper;

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":6}}";
        assertTrue(message.equals(diceWrapper.toJSON().toJSONString()));
    }

    @Test
    public void toObjectTest() {
        String message = "{\"color\":\"BLUE\",\"value\":6}";
        org.json.simple.parser.JSONParser jsonParser = new org.json.simple.parser.JSONParser();
        try {
            assertTrue((DiceWrapper.toObject((JSONObject) jsonParser.parse(message))).equals(diceWrapper));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        diceWrapper = new DiceWrapper(ColorWrapper.BLUE, 6);
    }
}