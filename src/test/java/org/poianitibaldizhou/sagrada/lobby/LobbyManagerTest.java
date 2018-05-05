package org.poianitibaldizhou.sagrada.lobby;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LobbyManagerTest {
    @DataPoint
    public static LobbyManager lobbyManager;

    @Mock
    public ILobbyObserver lobbyObserver1;

    @Mock
    public ILobbyObserver lobbyObserver2;

    @Mock
    public ILobbyObserver lobbyObserver3;

    @Mock
    public ILobbyObserver lobbyObserver4;

    @Mock
    public ILobbyObserver lobbyObserver5;

    @Mock
    public ILobbyObserver lobbyObserver6;


    @DataPoint
    public static List<ILobbyObserver> observers;

    @BeforeClass
    public static void setUpClass() {
        lobbyManager = new LobbyManager();
        observers = new ArrayList<>();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        observers.add(lobbyObserver1);
        observers.add(lobbyObserver2);
        observers.add(lobbyObserver3);
        observers.add(lobbyObserver4);
        observers.add(lobbyObserver5);
        observers.add(lobbyObserver6);
    }
    /**
     * Test the entire singleton class
     */
    @Test
    public void testSingletonClass() throws RemoteException {
        // Login test
        User user1 = new User("user1", lobbyManager.login("user1"));
        User user2 = new User("user2", lobbyManager.login("user2"));
        try {
            lobbyManager.login("user1");
            fail("Exception expected");
        }catch (RemoteException re) {
            assertTrue("Login failed", lobbyManager.usersSize() == 2);
            assertEquals("Login failed",user1, lobbyManager.getUserByToken(user1.getToken()));
            assertEquals("Login failed",user2, lobbyManager.getUserByToken(user2.getToken()));
            assertTrue("Login failed",user1.getToken() != user2.getToken());
        }

        // getUserByToken test
        try {
            lobbyManager.getUserByToken("prova");
            if(!(user1.getToken().equals("prova") || user2.getToken().equals("prova"))){
                fail("getUserByToken failed: Exception expected");
            }
        } catch(RemoteException re) {

        }

        // Logout test
        try {
            lobbyManager.logout("prova");
            if(!(user1.getToken().equals("prova") || user2.getToken().equals("prova"))){
                fail("Logout failed: Exception expected");
            }
        } catch (RemoteException re) {
            assertEquals("Logout failed", 2, lobbyManager.usersSize());
        }

        lobbyManager.logout(user1.getToken());
        lobbyManager.logout(user2.getToken());
        assertEquals("Logout failed", 0, lobbyManager.usersSize());

        // Test user join lobby
        ArrayList<User> users = new ArrayList<>();
        StringBuilder name = new StringBuilder("utente");
        for (int i = 0; i < 7; i++) {
            name.append(i);
            users.add(new User(name.toString(), lobbyManager.login(name.toString())));
            name = new StringBuilder("utente");
        }

        for (int i = 0; i <= 3; i++) {
            lobbyManager.userJoinLobby(observers.get(i), users.get(i));
            for (int j = 0; j <= i; j++) {
                verify(observers.get(j), times(1)).onUserJoin(users.get(i));
            }
        }
        for (int i = 4; i <= 5; i++) {
            lobbyManager.userJoinLobby(observers.get(i), users.get(i));
            for (int j = 0; j <= i; j++) {
                if(j<4)
                    verify(observers.get(j), times(0)).onUserJoin(users.get(i));
                else
                    verify(observers.get(j), times(1)).onUserJoin(users.get(i));
            }
        }
        try {
            lobbyManager.userJoinLobby(observers.get(5), users.get(5));
            fail("Exception expected");
        }catch (RemoteException re) {

        }

        for (int i = 0; i <= 5; i++) {
            if(i < 4)
                verify(observers.get(i), times(1)).onGameStart();
            else
                verify(observers.get(i), times(0)).onGameStart();
        }

        // Test user leave lobby
        lobbyManager.userLeaveLobby(users.get(5));
        for (int i = 0; i <= 5; i++) {
            if(i < 4)
                verify(observers.get(i), times(0)).onUserExit(users.get(5));
            else
                verify(observers.get(i), times(1)).onUserExit(users.get(5));
        }

        try {
            lobbyManager.userLeaveLobby(users.get(0));
            fail("Exception expected");
        } catch(RemoteException re) {

        }
    }
}
