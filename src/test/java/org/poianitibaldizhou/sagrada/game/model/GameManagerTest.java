package org.poianitibaldizhou.sagrada.game.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.ManagerMediator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GameManagerTest {
    private GameManager gameManager;
    private List<Game> gameList;
    private List<String> playerList;

    @Mock
    private ManagerMediator managerMediator;

    @Mock
    private Game game1;

    @Mock
    private Game game2;

    @Mock
    private Game game3;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gameManager = new GameManager(managerMediator);
        when(game1.getName()).thenReturn("game1");
        when(game2.getName()).thenReturn("game2");
        when(game3.getName()).thenReturn("game3");

        playerList = new ArrayList<>();
        playerList.add("player1");
        playerList.add("player2");
        playerList.add("player3");

        gameList = new ArrayList<>();
        gameList.add(game1);
        gameList.add(game2);
        gameList.add(game3);
    }

    @After
    public void tearDown() {
        gameManager = null;
    }

    @Test
    public void testRemove() {

    }

    @Test
    public void testJoinNotExistingGame() throws Exception {
        gameManager.addGame(game1, "game1");
        gameManager.joinGame("notExistingGame", playerList.get(0));
        List<Game> games = gameManager.getGames();
        assertEquals(1, games.size());
        assertEquals(game1.getName(), games.get(0).getName());
    }

    @Test(expected = RemoteException.class)
    public void testJoinFailAlreadyInAnotherGame() throws Exception {
        gameManager.addGame(game1, game1.getName());
        gameManager.addGame(game2, game2.getName());
        gameManager.joinGame(game1.getName(), playerList.get(0));
        gameManager.joinGame(game2.getName(), playerList.get(0));
    }

    @Test
    public void testJoinGame() throws Exception {
        List<String> list1 = new ArrayList<>();
        list1.add(playerList.get(0));
        list1.add(playerList.get(1));

        List<String> list2 = new ArrayList<>();
        list2.add(playerList.get(2));

        gameManager.addGame(game1, "game1");
        gameManager.addGame(game2, game2.getName());

        gameManager.joinGame(game1.getName(), playerList.get(0));
        gameManager.joinGame(game1.getName(), playerList.get(1));
        gameManager.joinGame(game2.getName(), playerList.get(2));

        assertEquals(list1, gameManager.getPlayersByGame(game1.getName()));
        assertEquals(list2, gameManager.getPlayersByGame(game2.getName()));
    }

    @Test
    public void testAdd() {
        gameManager.addGame(game1, "game1");
        gameManager.addGame(game2, "game2");
        gameManager.addGame(game3, "game3");
        gameManager.addGame(game3, "game3");
        List<Game> curr = gameManager.getGames();
        assertEquals(3, curr.size());
        int[] flags = new int[3];
        for (Game g : curr) {
            if (g.getName().equals("game1"))
                flags[0] += 1;
            if (g.getName().equals("game2"))
                flags[1] += 1;
            if (g.getName().equals("game3"))
                flags[2] += 1;
        }

        for (int i = 0; i < 3; i++) {
            flags[i] = 1;
        }
    }

    @Test
    public void removeGameTest() throws Exception {
        gameManager.addGame(game1, game1.getName());
        for(String player : playerList)
            gameManager.joinGame(game1.getName(), player);
        gameManager.terminateGame(game1.getName());
        assertEquals(0, gameManager.getGames().size());
        assertEquals(null,gameManager.getPlayersByGame(game1.getName()));
    }

    @Test
    public void removeNonExistingGame() throws Exception {
        gameManager.addGame(game1, game1.getName());
        List<Game> prevList = gameManager.getGames();
        gameManager.terminateGame("NonExistingGame");
        List<Game> newList = gameManager.getGames();
        assertEquals(prevList.size(), newList.size());
        for (int i = 0; i < prevList.size(); i++) {
            assertEquals(prevList.get(i).getName(), newList.get(i).getName());
        }
    }

}
