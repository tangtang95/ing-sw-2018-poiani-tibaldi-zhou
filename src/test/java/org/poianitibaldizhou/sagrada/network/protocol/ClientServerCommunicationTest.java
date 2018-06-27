package org.poianitibaldizhou.sagrada.network.protocol;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ClientServerCommunicationTest {

    @DataPoint
    public static ServerCreateMessage serverCreateMessage;

    @DataPoint
    public static ClientGetMessage clientGetMessage;

    @Test
    public void reconnectError() {
        assertTrue(clientGetMessage.hasReconnectError(serverCreateMessage.reconnectErrorMessage()));
        assertFalse(clientGetMessage.hasReconnectError(""));
    }

    @Before
    public void setUp() throws Exception {
        serverCreateMessage = new ServerCreateMessage();
        clientGetMessage = new ClientGetMessage();
    }
}
