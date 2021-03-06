package org.poianitibaldizhou.sagrada.lobby.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.network.observers.realobservers.ILobbyObserver;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LobbyManagerTest {
    private LobbyManager lobbyManager;

    @Mock
    private MediatorManager managerMediator;

    @Mock
    private ILobbyObserver lobbyObserver1, lobbyObserver2, lobbyObserver3,
            lobbyObserver4, lobbyObserver5, lobbyObserver6;

    private List<ILobbyObserver> observers;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lobbyManager = new LobbyManager(managerMediator);
        observers = new ArrayList<>();
        observers.add(lobbyObserver1);
        observers.add(lobbyObserver2);
        observers.add(lobbyObserver3);
        observers.add(lobbyObserver4);
        observers.add(lobbyObserver5);
        observers.add(lobbyObserver6);
    }

    @Test
    public void testGetLobbyNetworkManager() {
        assertNotNull(lobbyManager.getLobbyNetworkManager());
    }

    @Test
    public void testGetLobby(){
        String token1 = lobbyManager.login("u1");
        String token2 = lobbyManager.login("u2");

        Lobby lobbyExpected = new Lobby("randomName");
        lobbyExpected.join(new User("u1", token1));
        lobbyExpected.join(new User("u2", token2));

        lobbyManager.userJoinLobby(lobbyObserver1, new User("u1", token1));
        lobbyManager.userJoinLobby(lobbyObserver2, new User("u2", token2));

        Lobby lobbyInManager = lobbyManager.getLobby();

        assertEquals(lobbyExpected.getUserList(), lobbyInManager.getUserList());
    }

    @Test
    public void testLobbyObserverManager() {
        assertNull(lobbyManager.getLobbyObserverManager());
        String token = lobbyManager.login("username");
        lobbyManager.userJoinLobby(lobbyObserver1, new User("username", token));
        assertTrue(lobbyManager.getLobbyObserverManager().getDisconnectedUserNotNotified().isEmpty());
    }

    @After
    public void tearDown() {
        lobbyManager = null;
        observers = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserJoinWithoutLogin() {
        lobbyManager.userJoinLobby(lobbyObserver1, new User("u1", "t1"));
    }

    @Test
    public void testUserJoin() {
        String token1 = lobbyManager.login("u1");
        String token2 = lobbyManager.login("u2");

        lobbyManager.userJoinLobby(lobbyObserver1, new User("u1", token1));
        lobbyManager.userJoinLobby(lobbyObserver2, new User("u2", token2));

        assertEquals(2, lobbyManager.getLobbyUsers().size());
    }


    @Test
    public void testTimeoutNoGameStart() throws Exception {
        /*
        User user1 = new User("u1", lobbyManager.login("u1"));
        lobbyManager.userJoinLobby(observers.get(0), user1);
        long time1 = lobbyManager.getTimeToTimeout();
        Thread.sleep(time1 / 6);
        long time2 = lobbyManager.getTimeToTimeout();
        assertTrue(time1 > time2);
        assertTrue(lobbyManager.DELAY_TIME >= time1);
        Thread.sleep(time1 + (time1 / 6));

        assertTrue(lobbyManager.getLobbyUsers().size() == 1 && lobbyManager.getLobbyUsers().get(0).equals(user1));
        verify(observers.get(0), times(0)).onGameStart(ArgumentMatchers.any(String.class));*/
    }

    @Test
    public void testTimeoutGameStart() throws Exception {
        /*
        User user1 = new User("u1", lobbyManager.login("u1"));
        User user2 = new User("u2", lobbyManager.login("u2"));

        lobbyManager.userJoinLobby(observers.get(0), user1);
        lobbyManager.userJoinLobby(observers.get(1), user2);

        Thread.sleep(lobbyManager.DELAY_TIME * 2);

        when(managerMediator.createMultiPlayerGame(lobbyManager.getLobbyUsers())).thenReturn("gamename");

        assertTrue(lobbyManager.getLobbyUsers().isEmpty());*/
    }

    @Test(expected = Exception.class)
    public void noLobbyActiveGetTimeout() {
        lobbyManager.getTimeToTimeout();
    }

    @Test(expected = Exception.class)
    public void noLobbyActiveGetUsers() {
        lobbyManager.getLobbyUsers();
    }

    @Test
    public void testUserLeaveLobby()  {
        User user1 = new User("u1", lobbyManager.login("u1"));
        lobbyManager.userJoinLobby(observers.get(0), user1);
        lobbyManager.userLeaveLobby(user1);
        assertEquals(0, lobbyManager.getLoggedUser().size());
    }

    @Test
    public void lobbyActive() {
        User user1 = new User("user1", lobbyManager.login("user1"));

        assertFalse(lobbyManager.isLobbyActive());

        lobbyManager.userJoinLobby(observers.get(0), user1);

        assertTrue(lobbyManager.isLobbyActive());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUserByToken() {
        User user1 = new User("user1", lobbyManager.login("user1"));

        lobbyManager.getUserByToken("notthattoken");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUserDoubleJoin() {
        User user1 = new User("user1", lobbyManager.login("user1"));

        lobbyManager.userJoinLobby(observers.get(0), user1);
        lobbyManager.userJoinLobby(observers.get(1), user1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleLogin() {
        lobbyManager.login("test");
        lobbyManager.login("test");
    }

    @Test
    public void joinUntilLobbyFull() {
        for (int i = 0; i < Lobby.MAX_PLAYER; i++) {
            lobbyManager.userJoinLobby(observers.get(i), new User("name"+i, lobbyManager.login("name"+i)));
        }
        assertEquals(0, lobbyManager.getLobbyUsers().size());
        assertEquals(0, lobbyManager.getLoggedUser().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailLogout() {
        lobbyManager.userLeaveLobby(new User("t1", "t1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFailDisconnect() {
        lobbyManager.userDisconnects("token");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMediatorManagerPlayerAlreadyInGame() {
        when(managerMediator.isAlreadyPlayingAGame("username")).thenReturn(true);
        lobbyManager.login("username");
    }

    @Test
    public void testDisconnect() {
        String token = lobbyManager.login("test");
        User user = new User("test", token);
        lobbyManager.userJoinLobby(observers.get(0), user);
        lobbyManager.userDisconnects(token);
        assertTrue(lobbyManager.getLobbyUsers().isEmpty());
    }

    @Test
    public void testTimeout() {
        /*
        User user1 = new User("u1", lobbyManager.login("u1"));
        User user2 = new User("u2", lobbyManager.login("u2"));

        lobbyManager.userJoinLobby(observers.get(0), user1);
        lobbyManager.userJoinLobby(observers.get(1), user2);

        when(managerMediator.createMultiPlayerGame(lobbyManager.getLobbyUsers())).thenReturn("gamename");

        assertTrue(lobbyManager.getLobbyUsers().isEmpty());*/
    }
}
