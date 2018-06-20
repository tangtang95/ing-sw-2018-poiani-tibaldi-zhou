package org.poianitibaldizhou.sagrada.network.observers;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.lobby.model.User;
import org.poianitibaldizhou.sagrada.utilities.NetworkUtility;

import java.util.ArrayList;
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
        tokenList.add(NetworkUtility.encrypt("user1"));
        tokenList.add(NetworkUtility.encrypt("user2"));
        tokenList.add(NetworkUtility.encrypt("user3"));
        tokenList.add(NetworkUtility.encrypt("user4"));

        List<User> userList = new ArrayList<>();
        userList.add(new User("user1", NetworkUtility.encrypt("user1")));
        userList.add(new User("user2", NetworkUtility.encrypt("user2")));
        userList.add(new User("user3", NetworkUtility.encrypt("user3")));
        userList.add(new User("user4", NetworkUtility.encrypt("user4")));

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