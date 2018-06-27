package org.poianitibaldizhou.sagrada.lobby.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.IView;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.view.ILobbyView;
import org.poianitibaldizhou.sagrada.network.observers.LobbyObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.network.LobbyNetworkManager;
import org.poianitibaldizhou.sagrada.network.protocol.ClientCreateMessage;
import org.poianitibaldizhou.sagrada.network.protocol.ClientGetMessage;
import org.poianitibaldizhou.sagrada.network.protocol.wrapper.UserWrapper;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LobbyControllerTest {

    private ClientCreateMessage clientCreateMessage;
    private ClientGetMessage clientGetMessage;

    @Mock
    private LobbyManager lobbyManager;

    @Mock
    private LobbyNetworkManager lobbyNetworkManager;

    @Mock
    private ILobbyObserver observer1;

    @Mock
    private LobbyObserverManager lobbyObserverManager;

    @Mock
    private IView clientView1;

    @Mock
    private ILobbyView lobbyView;

    private LobbyController lobbyController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(lobbyManager.getLobbyNetworkManager()).thenReturn(lobbyNetworkManager);

        lobbyController = new LobbyController(lobbyManager);
        clientCreateMessage = new ClientCreateMessage();
        clientGetMessage = new ClientGetMessage();
    }

    @Test
    public void testLogin() throws Exception {
        String username = "username";
        when(lobbyManager.login(username)).thenReturn(NetworkUtility.encryptUsername(username));
        String response = lobbyController.login(clientCreateMessage.createUsernameMessage(username).buildMessage(), lobbyView);

        assertEquals(NetworkUtility.encryptUsername(username), clientGetMessage.getToken(response));
    }

    @Test
    public void failLogin() throws Exception {
        String username = "username";
        when(lobbyManager.login(username)).thenThrow(new IllegalArgumentException());

        String response = lobbyController.login(clientCreateMessage.createUsernameMessage(username).buildMessage(), lobbyView);

        assertEquals("", clientGetMessage.getToken(response));
        verify(clientView1).err(anyString());
    }

    @Test
    public void testLeave() throws Exception {
        String username = "username";
        String token = NetworkUtility.encryptUsername(username);
        User user = new User(username, token);

        when(lobbyManager.getUserByToken(token)).thenReturn(user);
        when(lobbyNetworkManager.getViewByToken(token)).thenReturn(clientView1);

        lobbyController.leave(clientCreateMessage.createUsernameMessage(username).createTokenMessage(token).buildMessage());

        verify(lobbyManager, times(1)).userLeaveLobby(user);
        verify(lobbyNetworkManager, times(1)).removeView(token);
    }

    @Test
    public void failLeave() throws Exception{
        String username = "username";
        String token = NetworkUtility.encryptUsername(username);
        User user = new User("ciccio", "pasticcio");

        when(lobbyManager.getUserByToken(token)).thenReturn(user);
        when(lobbyNetworkManager.getViewByToken(token)).thenReturn(clientView1);

        lobbyController.leave(clientCreateMessage.createUsernameMessage(username).createTokenMessage(token).buildMessage());

        verify(lobbyManager, times(0)).userLeaveLobby(user);
        verify(lobbyNetworkManager, times(0)).removeView(token);
    }

    @Test
    public void testJoin() throws Exception {
        String username = "username";
        String token = NetworkUtility.encryptUsername(username);
        User user = new User(username, token);

        when(lobbyManager.getUserByToken(token)).thenReturn(user);
        when(lobbyNetworkManager.getViewByToken(token)).thenReturn(clientView1);
        lobbyController.join(clientCreateMessage.createUsernameMessage(username).createTokenMessage(token).buildMessage(), observer1);
        verify(lobbyManager, times(1)).userJoinLobby(observer1, user);
    }

    @Test
    public void testGetUsers() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User("user1", NetworkUtility.encryptUsername("user1")));
        users.add(new User("user2", NetworkUtility.encryptUsername("user2")));
        users.add(new User("user3", NetworkUtility.encryptUsername("user3")));

        when(lobbyManager.getLobbyUsers()).thenReturn(users);

        String response = lobbyController.getUsersInLobby();

        List<UserWrapper> responseUsers = clientGetMessage.getListOfUserWrapper(response);

        assertEquals(users.size(), responseUsers.size());

        List<UserWrapper> expected = users.stream().map(user -> new UserWrapper(user.getName())).collect(Collectors.toList());

        assertEquals(expected, responseUsers);
    }

    @Test
    public void testGetTimeout() throws Exception {
        when(lobbyManager.getTimeToTimeout()).thenReturn(new Long(60000));

        String response = lobbyController.getTimeout();

        assertEquals("01:00", clientGetMessage.getTimeout(response));
    }

    @Test
    public void testHandleIOException() throws Exception {
        String username = "username";
        String token = NetworkUtility.encryptUsername(username);
        User user = new User(username, token);

        when(lobbyManager.getUserByToken(token)).thenReturn(user);
        when(lobbyManager.getLobbyObserverManager()).thenReturn(lobbyObserverManager);
        when(lobbyNetworkManager.getViewByToken(token)).thenReturn(clientView1);
        doThrow(IOException.class).
                when(clientView1).ack(anyString());

        lobbyController.join(clientCreateMessage.createUsernameMessage(username).createTokenMessage(token).buildMessage(), observer1);
        verify(lobbyManager, times(1)).userJoinLobby(observer1, user);
        verify(lobbyObserverManager).signalDisconnection(token);
    }

}
