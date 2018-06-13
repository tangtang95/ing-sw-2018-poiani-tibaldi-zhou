package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static org.junit.Assert.*;

public class JSONProtocolTest {

    @DataPoint
    public static JSONProtocol jsonProtocol;

    @Before
    public void setUp() throws Exception {
        jsonProtocol = new JSONProtocol();
    }

    @Test(expected = Exception.class)
    public void appendMessage() {
        jsonProtocol.appendMessage("test", new Object());
    }

}