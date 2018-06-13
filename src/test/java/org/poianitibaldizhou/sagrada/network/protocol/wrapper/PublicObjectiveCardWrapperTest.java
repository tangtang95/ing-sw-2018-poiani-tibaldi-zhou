package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PublicObjectiveCardWrapperTest {

    @DataPoint
    public static PublicObjectiveCardWrapper publicObjectiveCardWrapper;

    @Before
    public void setUp() throws Exception {
        publicObjectiveCardWrapper = new PublicObjectiveCardWrapper("test", "test", 1);
    }

    @Test
    public void toJsonTest() {
        assertEquals(null, publicObjectiveCardWrapper.toJSON());
    }

    @Test
    public void toObjectTest() {
        String message = "{\"name\":\"test\",\"description\":\"test\",\"cardPoint\":1}";
        JSONParser jsonParser = new JSONParser();
        try {
            assertEquals(publicObjectiveCardWrapper,
                    PublicObjectiveCardWrapper.toObject((JSONObject) jsonParser.parse(message)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCardPoint() {
        assertEquals(1, publicObjectiveCardWrapper.getCardPoint());
    }

    @Test
    public void hashCodeTest() {
        PublicObjectiveCardWrapper pub = new PublicObjectiveCardWrapper("test", "test", 4);
        assertNotEquals(pub.hashCode(), publicObjectiveCardWrapper.hashCode());
    }
}