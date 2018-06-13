package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PrivateObjectiveCardWrapperTest {

    @DataPoint
    public static PrivateObjectiveCardWrapper privateObjectiveCardWrapper;

    @Before
    public void setUp() throws Exception {
        privateObjectiveCardWrapper = new PrivateObjectiveCardWrapper("test", "test", ColorWrapper.YELLOW);
    }

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"privateObjectiveCard\",\"body\":{\"color\":\"YELLOW\",\"name\":\"test\",\"description\":\"test\"}}";
        assertEquals(message, privateObjectiveCardWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        String message = "{\"color\":\"YELLOW\",\"name\":\"test\",\"description\":\"test\"}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(privateObjectiveCardWrapper,
                    PrivateObjectiveCardWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hashCodeTest() {
        PrivateObjectiveCardWrapper privateObjectiveCard = new PrivateObjectiveCardWrapper("test1", "test", ColorWrapper.YELLOW);
        assertNotEquals(privateObjectiveCard.hashCode(), privateObjectiveCardWrapper.hashCode());
    }
}