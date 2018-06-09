package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class TileWrapperTest {

    @DataPoint
    public static TileWrapper tileWrapper;

    @Before
    public void setUp() throws Exception {
        tileWrapper = new TileWrapper("4", new DiceWrapper(ColorWrapper.BLUE, 4));
    }

    @Test
    public void toObjectTest() {
        String message = "{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}},\"constraint\":4}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(tileWrapper, TileWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"tile\",\"body\":{\"dice\":{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":4}},\"constraint\":4}}";
        assertEquals(message, tileWrapper.toJSON().toJSONString());
    }
}