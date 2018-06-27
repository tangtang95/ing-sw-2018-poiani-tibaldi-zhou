package org.poianitibaldizhou.sagrada.network.observers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GameObserverManagerTest {


    @Mock
    private IGame game;

    private GameObserverManager gameObserverManager;
    private List<String> tokenList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tokenList = new ArrayList<>();
        tokenList.add(NetworkUtility.encryptUsername("user1"));
        tokenList.add(NetworkUtility.encryptUsername("user2"));
        tokenList.add(NetworkUtility.encryptUsername("user3"));
        tokenList.add(NetworkUtility.encryptUsername("user4"));

        List<User> userList = new ArrayList<>();
        userList.add(new User("user1", NetworkUtility.encryptUsername("user1")));
        userList.add(new User("user2", NetworkUtility.encryptUsername("user2")));
        userList.add(new User("user3", NetworkUtility.encryptUsername("user3")));
        userList.add(new User("user4", NetworkUtility.encryptUsername("user4")));

        when(game.getUsers()).thenReturn(userList);

        gameObserverManager = new GameObserverManager(tokenList, game);
    }

    @Test
    public void testDisconnection() {
        gameObserverManager.signalDisconnection(tokenList.get(0));
        assertEquals(Collections.singleton(tokenList.get(0)), gameObserverManager.getDisconnectedPlayer());
        assertEquals(Collections.singleton(tokenList.get(0)), gameObserverManager.getDisconnectedPlayerNotNotified());
    }

    @Test
    public void testDisconnectionNotified() {
        gameObserverManager.signalDisconnection(tokenList.get(0));
        gameObserverManager.notifyDisconnection(tokenList.get(0));
        assertTrue(gameObserverManager.getDisconnectedPlayerNotNotified().isEmpty());
        assertEquals(Collections.singleton(tokenList.get(0)), gameObserverManager.getDisconnectedPlayer());
    }


    @Test
    public void testReconnect() {
        gameObserverManager.signalDisconnection(tokenList.get(0));
        gameObserverManager.signalDisconnection(tokenList.get(1));

        gameObserverManager.signalReconnect(tokenList.get(1));

        assertEquals(Collections.singleton(tokenList.get(0)), gameObserverManager.getDisconnectedPlayer());
    }

}