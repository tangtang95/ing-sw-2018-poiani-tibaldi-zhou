package org.poianitibaldizhou.sagrada.network.observers;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.IGame;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class GameObserverManagerTest {


    @Mock
    private IGame game;

    private GameObserverManager gameObserverManager;
    private List<String> tokenList;
    private List<User> userList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        tokenList = new ArrayList<>();
        tokenList.add(String.valueOf("user1".hashCode()));
        tokenList.add(String.valueOf("user2".hashCode()));
        tokenList.add(String.valueOf("user3".hashCode()));
        tokenList.add(String.valueOf("user4".hashCode()));

        userList = new ArrayList<>();
        userList.add(new User("user1", String.valueOf("user1".hashCode())));
        userList.add(new User("user2", String.valueOf("user2".hashCode())));
        userList.add(new User("user3", String.valueOf("user3".hashCode())));
        userList.add(new User("user4", String.valueOf("user4".hashCode())));

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