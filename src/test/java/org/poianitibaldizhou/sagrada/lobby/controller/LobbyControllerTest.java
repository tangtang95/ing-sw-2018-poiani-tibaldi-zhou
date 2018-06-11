package org.poianitibaldizhou.sagrada.lobby.controller;

import com.sun.org.glassfish.gmbal.ManagedObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LobbyControllerTest {

    private ClientCreateMessage clientCreateMessage;
    private ClientGetMessage clientGetMessage;

    @Mock
    private LobbyManager lobbyManager;

    @Mock
    private IView clientView1;

    private LobbyController lobbyController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        lobbyController = new LobbyController(lobbyManager);
        clientCreateMessage = new ClientCreateMessage();
        clientGetMessage = new ClientGetMessage();
    }

    @Test
    public void testLogin() throws Exception {
        String username = "username";
        when(lobbyManager.login(username)).thenReturn(String.valueOf(username.hashCode()));
        String response = lobbyController.login(clientCreateMessage.createUsernameMessage(username).buildMessage(), clientView1);

        assertEquals(String.valueOf(username.hashCode()), clientGetMessage.getToken(response));
    }

    @Test
    public void failLogin() throws Exception {
        String username = "username";
        when(lobbyManager.login(username)).thenThrow(new IllegalArgumentException());

        String response = lobbyController.login(clientCreateMessage.createUsernameMessage(username).buildMessage(), clientView1);

        assertEquals("", clientGetMessage.getToken(response));
        verify(clientView1).err(anyString());
    }


}
