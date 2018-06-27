package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class UseToolCardStateWrapperTest {

    @DataPoint
    public static UseToolCardActionWrapper useToolCardStateWrapper = new UseToolCardActionWrapper();

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"useToolCardAction\",\"body\":{}}";
        assertEquals(message, useToolCardStateWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        assertEquals(null, UseToolCardActionWrapper.toObject());
    }

}