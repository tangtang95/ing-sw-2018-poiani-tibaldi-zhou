package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class EndTurnStateWrapperTest {

    @DataPoint
    public static EndTurnStateWrapper endTurnStateWrapper = new EndTurnStateWrapper();

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"endTurnAction\",\"body\":{}}";
        assertEquals(message, endTurnStateWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        assertEquals(null, EndTurnStateWrapper.toObject());
    }
}