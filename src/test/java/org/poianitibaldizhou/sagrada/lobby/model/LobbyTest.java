package org.poianitibaldizhou.sagrada.lobby.model;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.ILobbyFakeObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LobbyTest {
    @Mock
    public ILobbyFakeObserver lobbyObserver1, lobbyObserver2, lobbyObserver3, lobbyObserver4, lobbyObserver5, lobbyObserver6;

    @DataPoint
    public ArrayList<ILobbyFakeObserver> observers;

    @DataPoint
    public Lobby lobby;

    @DataPoint
    public ArrayList<User> users;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lobby = new Lobby("Lobby test");

        observers = new ArrayList<>();

        users = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            users.add(new User("user" + i, UUID.randomUUID().toString()));
        }

        observers.add(lobbyObserver1);
        observers.add(lobbyObserver2);
        observers.add(lobbyObserver3);
        observers.add(lobbyObserver4);
        observers.add(lobbyObserver5);
        observers.add(lobbyObserver6);
    }

    @Test
    public void testGetName() {
        String lobbyName = "lobbyName";
        Lobby tempLobby = new Lobby(lobbyName);
        assertEquals(lobbyName, tempLobby.getName());
    }

    /**
     * Tests the call on the fake observers when an user join the lobby
     */
    @Test
    public void testJoinObserverCall() {
        lobby.attachObserver(users.get(0).getToken(), observers.get(0));
        lobby.join(users.get(0));

        lobby.attachObserver(users.get(1).getToken(), observers.get(1));
        lobby.join(users.get(1));

        verify(observers.get(0), times(1)).onUserJoin(users.get(0));
        verify(observers.get(0), times(1)).onUserJoin(users.get(1));
        verify(observers.get(1), times(1)).onUserJoin(users.get(1));
    }

    /**
     * Test user join
     */
    @Test
    public void testJoin() {
        List<User> testUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            testUsers.add(users.get(i));
        }

        lobby.join(users.get(0));
        lobby.join(users.get(1));
        lobby.join(users.get(2));

        assertEquals(testUsers, lobby.getUserList());
    }

    /**
     * Tests the call on the fake observers when an user leave the lobby
     */
    @Test
    public void testExitObserverCall() {
        lobby.attachObserver(users.get(0).getToken(), observers.get(0));
        lobby.join(users.get(0));

        lobby.attachObserver(users.get(1).getToken(), observers.get(1));
        lobby.join(users.get(1));

        lobby.leave(users.get(1));

        lobby.getLobbyObserverMap().forEach((key, value) -> {
            verify(value, times(1)).onUserExit(users.get(1));
        });
    }

    /**
     * Test user exit
     */
    @Test
    public void testUserExit() {
        lobby.join(users.get(0));
        lobby.leave(users.get(0));

        assertTrue(lobby.getUserList().isEmpty());
    }

    /**
     * Test user join: lobby full
     */
    @Test
    public void testUserJoinFull() {
        for (int i = 0; i < Lobby.MAX_PLAYER; i++) {
            if (i < Lobby.MAX_PLAYER - 1)
                assertEquals(false, lobby.join(new User("name" + i, "token" + i)));
            else
                assertEquals(true, lobby.join(new User("name" + i, "token" + i)));
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testUserJoinWithException() {
        for (int i = 0; i < Lobby.MAX_PLAYER + 1; i++) {
            lobby.join(new User("" + i, "" + i));
        }
    }

    /**
     * Test game start
     */
    @Test
    public void testGameStartObserverCall() {
        for (int i = 0; i < 2; i++) {
            lobby.attachObserver("token" + i, observers.get(i));
            lobby.join(new User("name" + i, "token" + i));
        }

        assertFalse(lobby.isGameStarted());

        lobby.gameStart("game name");

        for (int i = 0; i < 2; i++) {
            verify(observers.get(i), times(1)).onGameStart("game name");
        }
        assertTrue(lobby.isGameStarted());
    }


    /**
     * Test observer detach
     */
    @Test
    public void testObserverDetach() {
        lobby.attachObserver("0", observers.get(0));
        lobby.attachObserver("1", observers.get(1));
        lobby.detachObserver("0");
        lobby.join(users.get(1));

        verify(observers.get(0), times(0)).onUserJoin(users.get(1));
        verify(observers.get(1), times(1)).onUserJoin(users.get(1));
    }
}
