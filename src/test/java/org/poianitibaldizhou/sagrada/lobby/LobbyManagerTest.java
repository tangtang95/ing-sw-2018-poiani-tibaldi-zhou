package org.poianitibaldizhou.sagrada.lobby;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.ManagerMediator;
import org.poianitibaldizhou.sagrada.lobby.model.ILobbyObserver;
import org.poianitibaldizhou.sagrada.lobby.model.LobbyManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LobbyManagerTest {
    private LobbyManager lobbyManager;

    @Mock
    private ManagerMediator managerMediator;

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

    @After
    public void tearDown() {
        lobbyManager = null;
        observers = null;
    }

    @Test
    public void test() {

    }

    /*
    @Test
    public void testLobbyManager() throws RemoteException {
        // Login test
        /*User user1 = new User("user1", lobbyManager.login("user1"));
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
            lobbyManager.logout("notexistinguser");
            fail("Exception expected");
        } catch(RemoteException re) {
        }
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

    @Test
    public void testTimeoutNoGameStart() throws RemoteException {
        User user1 = new User("u1", lobbyManager.login("u1"));
        lobbyManager.userJoinLobby(observers.get(0), user1);
        long time1 = lobbyManager.getTimeToTimeout();
        try {
            Thread.sleep(time1 / 6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time2 = lobbyManager.getTimeToTimeout();
        assertTrue(time1 > time2);
        assertTrue(lobbyManager.getDelayTime() >= time1);
        try {
            Thread.sleep(time1 + (time1/6));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(lobbyManager.getLobbyUsers().size() == 1 && lobbyManager.getLobbyUsers().get(0).equals(user1));
        verify(observers.get(0), times(0)).onGameStart();
    }

    @Test
    public void testTimeoutGameStart() throws RemoteException {
        User user1 = new User("u1", lobbyManager.login("u1"));
        User user2 = new User("u2", lobbyManager.login("u2"));

        lobbyManager.userJoinLobby(observers.get(0), user1);
        lobbyManager.userJoinLobby(observers.get(1), user2);

        try {
            Thread.sleep(lobbyManager.getDelayTime()*3/2);
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }
        verify(observers.get(0), times(1)).onGameStart();
        verify(observers.get(1), times(1)).onGameStart();
    }

    @Test
    public void noLobbyActive() {
        try {
            lobbyManager.getTimeToTimeout();
            fail("Exception expected");
        } catch (RemoteException e) {

        }

        try {
            lobbyManager.getLobbyUsers();
            fail("Exception expected");
        } catch (RemoteException e) {

        }
    }

    @Test
    public void lobbyGetBackToZeroPlayer() throws RemoteException {
        User user1 = new User("u1", lobbyManager.login("u1"));
        lobbyManager.userJoinLobby(observers.get(0), user1);
        try {
            Thread.sleep(lobbyManager.getDelayTime() / 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long time1 = lobbyManager.getTimeToTimeout();
        lobbyManager.userLeaveLobby(user1);
        lobbyManager.userJoinLobby(observers.get(0), user1);
        assertTrue(lobbyManager.getTimeToTimeout() > time1);
    }*/
}
