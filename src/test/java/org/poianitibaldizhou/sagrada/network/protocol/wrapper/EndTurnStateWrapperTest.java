package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class EndTurnStateWrapperTest {

    @DataPoint
    public static EndTurnActionWrapper endTurnStateWrapper = new EndTurnActionWrapper();

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"endTurnAction\",\"body\":{}}";
        assertEquals(message, endTurnStateWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        assertEquals(null, EndTurnActionWrapper.toObject());
    }
}