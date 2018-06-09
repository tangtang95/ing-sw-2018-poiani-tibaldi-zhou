package org.poianitibaldizhou.sagrada.network.protocol.wrapper;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class PlaceDiceStateWrapperTest {

    @DataPoint
    public static PlaceDiceStateWrapper placeDiceStateWrapper = new PlaceDiceStateWrapper();

    @Test
    public void toJsonTest() {
        String message = "{\"type\":\"placeDiceAction\",\"body\":{}}";
        assertEquals(message, placeDiceStateWrapper.toJSON().toJSONString());
    }

    @Test
    public void toObjectTest() {
        assertEquals(null, PlaceDiceStateWrapper.toObject());
    }
}