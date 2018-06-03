package org.poianitibaldizhou.sagrada.lobby;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.poianitibaldizhou.sagrada.lobby.model.observers.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.Lobby;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class LobbyTest {
    @Mock
    public static ILobbyObserver lobbyObserver1;

    @Mock
    public static ILobbyObserver lobbyObserver2;

    @Mock
    public static ILobbyObserver lobbyObserver3;

    @Mock
    public static ILobbyObserver lobbyObserver4;

    @Mock
    public static ILobbyObserver lobbyObserver5;

    @Mock
    public static ILobbyObserver lobbyObserver6;

    @DataPoint
    public static ArrayList<ILobbyObserver> observers;

    @DataPoint
    public static Lobby lobby;

    @DataPoint
    public static ArrayList<User> users;

    @BeforeClass
    public static void setUpClass() {
        observers = new ArrayList<>();

        users = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            users.add(new User("user"+i, UUID.randomUUID().toString()));
        }
    }
/*
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        lobby = new Lobby("Lobby test");
        observers.add(lobbyObserver1);
        observers.add(lobbyObserver2);
        observers.add(lobbyObserver3);
        observers.add(lobbyObserver4);
        observers.add(lobbyObserver5);
        observers.add(lobbyObserver6);

    }

    @After
    public void tearDown() {
        lobby = null;
    }

    @AfterClass
    public static void tearDownClass() {
        observers = null;
        users = null;
    }

    @Test
    public void testUserJoinGamestartLeave() throws RemoteException {
        for (int i = 0; i < Lobby.MAX_PLAYER; i++) {
            lobby.attachObserver(observers.get(i));
        }

        boolean flag;

        for (int i = 0; i < Lobby.MAX_PLAYER; i++) {
            System.out.println(users.get(i));
            flag = lobby.join(users.get(i));
            for(int j = 0; j <= i; j++)
                verify(observers.get(j), times(1)).onUserJoin(users.get(i));
            if(i < Lobby.MAX_PLAYER-1) {
                assertEquals(false, flag);
            } else {
                assertEquals(true, flag);
            }
        }


        lobby.gameStart();
        for (int i = 0; i < Lobby.MAX_PLAYER; i++) {
            verify(observers.get(i), times(1)).onGameStart();
        }

        lobby.leave(users.get(0));
        for(int i = 0; i < Lobby.MAX_PLAYER; i++) {
            verify(observers.get(i), times(1)).onUserExit(users.get(0));
        }
    }*/
}
