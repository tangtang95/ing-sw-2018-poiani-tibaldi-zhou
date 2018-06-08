package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DraftPoolWrapperTest {

    @DataPoint
    public static DraftPoolWrapper draftPoolWrapper;

    @Test
    public void toJsonTest() {
        assertEquals(null, draftPoolWrapper.toJSON());
    }

    @Before
    public void setUp() throws Exception {
        List<DiceWrapper> diceWrappers = new ArrayList<>();
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,5));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,2));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,1));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,6));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,3));
        diceWrappers.add(new DiceWrapper(ColorWrapper.BLUE,6));

        draftPoolWrapper = new DraftPoolWrapper(diceWrappers);
    }

    @Test
    public void toObjectTest() {
        String message = "{\"diceList\":{\"type\":\"collection\",\"body\":" +
                "[{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":5}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":2}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":1}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":6}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":3}}," +
                "{\"type\":\"dice\",\"body\":{\"color\":\"BLUE\",\"value\":6}}]}}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(draftPoolWrapper, DraftPoolWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}