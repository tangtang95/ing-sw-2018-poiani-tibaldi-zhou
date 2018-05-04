package org.poianitibaldizhou.sagrada.lobby;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyDatabase;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.lobby.view.CLILobbyView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LobbyDatabaseTest {
    @DataPoint
    public static LobbyDatabase lobbyDatabase;

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
        lobbyDatabase = LobbyDatabase.getInstance();
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
        User user1 = new User("user1", lobbyDatabase.login("user1"));
        User user2 = new User("user2", lobbyDatabase.login("user2"));
        try {
            lobbyDatabase.login("user1");
            fail("Exception expected");
        }catch (RemoteException re) {
            assertTrue("Login failed", lobbyDatabase.usersSize() == 2);
            assertEquals("Login failed",user1, lobbyDatabase.getUserByToken(user1.getToken()));
            assertEquals("Login failed",user2, lobbyDatabase.getUserByToken(user2.getToken()));
            assertTrue("Login failed",user1.getToken() != user2.getToken());
        }

        // getUserByToken test
        try {
            lobbyDatabase.getUserByToken("prova");
            if(!(user1.getToken().equals("prova") || user2.getToken().equals("prova"))){
                fail("getUserByToken failed: Exception expected");
            }
        } catch(RemoteException re) {

        }

        // Logout test
        try {
            lobbyDatabase.logout("prova");
            if(!(user1.getToken().equals("prova") || user2.getToken().equals("prova"))){
                fail("Logout failed: Exception expected");
            }
        } catch (RemoteException re) {
            assertEquals("Logout failed", 2,lobbyDatabase.usersSize());
        }

        lobbyDatabase.logout(user1.getToken());
        lobbyDatabase.logout(user2.getToken());
        assertEquals("Logout failed", 0, lobbyDatabase.usersSize());

        // Test user join lobby
        ArrayList<User> users = new ArrayList<>();
        String name = "utente";
        for (int i = 0; i < 7; i++) {
            name = new String(name + i);
            users.add(new User(name, lobbyDatabase.login(name)));
            name = "utente";
        }

        for (int i = 0; i <= 3; i++) {
            lobbyDatabase.userJoinLobby(observers.get(i), users.get(i));
            for (int j = 0; j <= i; j++) {
                verify(observers.get(j), times(1)).onUserJoin(users.get(i));
            }
        }
        for (int i = 4; i <= 5; i++) {
            lobbyDatabase.userJoinLobby(observers.get(i), users.get(i));
            for (int j = 0; j <= i; j++) {
                if(j<4)
                    verify(observers.get(j), times(0)).onUserJoin(users.get(i));
                else
                    verify(observers.get(j), times(1)).onUserJoin(users.get(i));
            }
        }
        try {
            lobbyDatabase.userJoinLobby(observers.get(5), users.get(5));
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
        lobbyDatabase.userLeaveLobby(users.get(5));
        for (int i = 0; i <= 5; i++) {
            if(i < 4)
                verify(observers.get(i), times(0)).onUserExit(users.get(5));
            else
                verify(observers.get(i), times(1)).onUserExit(users.get(5));
        }

        try {
            lobbyDatabase.userLeaveLobby(users.get(0));
            fail("Exception expected");
        } catch(RemoteException re) {

        }
    }
}
