package org.poianitibaldizhou.sagrada.game.model;

import edu.emory.mathcs.backport.java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.MediatorManager;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.coin.FavorToken;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NoConstraint;
import org.poianitibaldizhou.sagrada.game.model.players.MultiPlayer;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.network.observers.GameObserverManager;
import org.poianitibaldizhou.sagrada.lobby.model.User;

import javax.xml.validation.Schema;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameManagerTest {
    private GameManager gameManager;
    private List<IGame> gameList;
    private List<String> playerList;

    @Mock
    private MediatorManager managerMediator;

    @Mock
    private IGame game1;

    @Mock
    private IGame game2;

    @Mock
    private IGame game3;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gameManager = new GameManager(managerMediator);
        when(game1.getName()).thenReturn("game1");
        when(game2.getName()).thenReturn("game2");
        when(game3.getName()).thenReturn("game3");

        when(game1.isSinglePlayer()).thenReturn(false);
        when(game2.isSinglePlayer()).thenReturn(false);
        when(game3.isSinglePlayer()).thenReturn(true);

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
        gameManager.createMultiPlayerGame(game1, "game1");
        List<IGame> games = gameManager.getGameList();
        assertEquals(1, games.size());
        assertEquals(game1.getName(), games.get(0).getName());
    }

    @Test
    public void testJoinGame() throws Exception {
        List<User> list1 = new ArrayList<>();
        list1.add(new User(playerList.get(0), String.valueOf(playerList.get(0).hashCode())));
        list1.add(new User(playerList.get(1), String.valueOf(playerList.get(1).hashCode())));

        List<User> list2 = new ArrayList<>();
        list2.add(new User(playerList.get(2), String.valueOf(playerList.get(2).hashCode())));

        when(game1.getUsers()).thenReturn(list1);
        when(game2.getUsers()).thenReturn(list2);

        gameManager.createMultiPlayerGame(game1, "game1");
        gameManager.createMultiPlayerGame(game2, game2.getName());

        assertEquals(list1.stream().map(user -> user.getToken()).collect(Collectors.toList()), gameManager.getPlayersByGame(game1.getName()));
        assertEquals(list2.stream().map(user -> user.getToken()).collect(Collectors.toList()), gameManager.getPlayersByGame(game2.getName()));
    }

    @Test
    public void testAdd() {
        gameManager.createMultiPlayerGame(game1, "game1");
        gameManager.createMultiPlayerGame(game2, "game2");
        List<IGame> curr = gameManager.getGameList();
        assertEquals(2, curr.size());
        int[] flags = new int[3];
        for (IGame g : curr) {
            if (g.getName().equals("game1"))
                flags[0] += 1;
            if (g.getName().equals("game2"))
                flags[1] += 1;
        }

        for (int i = 0; i < 3; i++) {
            flags[i] = 1;
        }
    }

    @Test
    public void removeGameTest() throws Exception {
        gameManager.createMultiPlayerGame(game1, game1.getName());
        gameManager.terminateGame(game1.getName());
        assertEquals(0, gameManager.getGameList().size());
        assertEquals(null,gameManager.getPlayersByGame(game1.getName()));
    }

    @Test
    public void removeNonExistingGame() throws Exception {
        gameManager.createMultiPlayerGame(game1, game1.getName());
        List<IGame> prevList = gameManager.getGameList();
        gameManager.terminateGame("NonExistingGame");
        List<IGame> newList = gameManager.getGameList();
        assertEquals(prevList.size(), newList.size());
        for (int i = 0; i < prevList.size(); i++) {
            assertEquals(prevList.get(i).getName(), newList.get(i).getName());
        }
    }

    @Test(expected = Exception.class)
    public void testCreateMultiPlayerException() {
        gameManager.createMultiPlayerGame(game3, game3.getName());
    }

    @Test
    public void testContainsGameName() {
        gameManager.createMultiPlayerGame(game1, game1.getName());

        assertFalse(gameManager.notContainsGame(game1.getName()));
        assertTrue(gameManager.notContainsGame(game2.getName()));

        assertNull(gameManager.getGameByName(game2.getName()));
    }

    @Test(expected = Exception.class)
    public void testCreateSinglePlayerAlreadyWaiting() throws Exception {
        String username = "username";
        when(managerMediator.isAlreadyWaitingInALobby(username)).thenReturn(true);
        gameManager.createSinglePlayerGame(username, 2);
    }

    @Test(expected = Exception.class)
    public void testCreateSinglePlayerAlreadyInGame() throws Exception{
        List<User> list1 = new ArrayList<>();
        list1.add(new User(playerList.get(0), String.valueOf(playerList.get(0).hashCode())));
        list1.add(new User(playerList.get(1), String.valueOf(playerList.get(1).hashCode())));

        when(game1.getUsers()).thenReturn(list1);
        gameManager.createMultiPlayerGame(game1, game1.getName());

        gameManager.createSinglePlayerGame(list1.get(0).getName(), 4);
    }

    @Test
    public void testCreateSinglePlayer() throws Exception {
        String username = "username";
        String token = String.valueOf(username.hashCode());
        String gameName = gameManager.createSinglePlayerGame("username", 2);

        assertEquals(Collections.singletonList(token), gameManager.getPlayersByGame(gameName));
    }

    @Test
    public void testHandleEndGameSinglePlayerTrue() throws Exception {
        String username  = "username";
        String token = String.valueOf(username.hashCode());
        String gameName = gameManager.createSinglePlayerGame(username, 2);

        GameObserverManager gameObserverManager = gameManager.getObserverManagerByGame(gameName);
        gameObserverManager.signalDisconnection(token);

        assertTrue(gameManager.handleEndGame(gameManager.getGameByName(gameName), gameObserverManager));

        assertNull(gameManager.getGameByName(gameName));
    }

    @Test
    public void testHandleEndGameFalse() throws Exception {
        String username  = "username";
        String gameName = gameManager.createSinglePlayerGame(username, 2);

        assertFalse(gameManager.handleEndGame(gameManager.getGameByName(gameName), gameManager.getObserverManagerByGame(gameName)));

        assertNotNull(gameManager.getGameByName(gameName));
    }

    @Test
    public void testHandleEndGameMultiPlayerNoPlayerLeft() throws Exception {
        List<User> list1 = new ArrayList<>();
        list1.add(new User(playerList.get(0), String.valueOf(playerList.get(0).hashCode())));
        list1.add(new User(playerList.get(1), String.valueOf(playerList.get(1).hashCode())));

        when(game1.getUsers()).thenReturn(list1);
        gameManager.createMultiPlayerGame(game1, game1.getName());

        gameManager.getObserverManagerByGame(game1.getName()).signalDisconnection(String.valueOf(playerList.get(0).hashCode()));
        gameManager.getObserverManagerByGame(game1.getName()).signalDisconnection(String.valueOf(playerList.get(1).hashCode()));

        assertTrue(gameManager.handleEndGame(game1, gameManager.getObserverManagerByGame(game1.getName())));

        assertNull(gameManager.getGameByName(game1.getName()));
    }

    @Test
    public void testHandleEndGameMultiPlayerOnePlayerLeft() throws Exception {
        List<User> list1 = new ArrayList<>();
        list1.add(new User(playerList.get(0), String.valueOf(playerList.get(0).hashCode())));
        list1.add(new User(playerList.get(1), String.valueOf(playerList.get(1).hashCode())));

        IConstraint[][] constraints = new IConstraint[SchemaCard.NUMBER_OF_ROWS][SchemaCard.NUMBER_OF_COLUMNS];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                constraints[i][j] = new NoConstraint();
            }
        }

        SchemaCard schemaCard = new SchemaCard("name", 2, constraints);

        List<Player> playerList = new ArrayList<>();
        playerList.add(new MultiPlayer(list1.get(0), new FavorToken(1), schemaCard,
                new ArrayList<PrivateObjectiveCard>()));
        playerList.add(new MultiPlayer(list1.get(1), new FavorToken(1), schemaCard,
                new ArrayList<PrivateObjectiveCard>()));

        when(game1.getUsers()).thenReturn(list1);
        when(game1.getPlayers()).thenReturn(playerList);
        gameManager.createMultiPlayerGame(game1, game1.getName());

        gameManager.getObserverManagerByGame(game1.getName()).signalDisconnection(list1.get(0).getToken());

        assertTrue(gameManager.handleEndGame(game1, gameManager.getObserverManagerByGame(game1.getName())));

        assertNull(gameManager.getGameByName(game1.getName()));
    }

    @Test
    public void testHandleEndGameMultiPlayerFalse() throws Exception {
        List<User> list1 = new ArrayList<>();
        list1.add(new User(playerList.get(0), String.valueOf(playerList.get(0).hashCode())));
        list1.add(new User(playerList.get(1), String.valueOf(playerList.get(1).hashCode())));
        list1.add(new User(playerList.get(2), String.valueOf(playerList.get(2).hashCode())));

        when(game1.getUsers()).thenReturn(list1);
        gameManager.createMultiPlayerGame(game1, game1.getName());

        gameManager.getObserverManagerByGame(game1.getName()).signalDisconnection(String.valueOf(playerList.get(0).hashCode()));

        assertFalse(gameManager.handleEndGame(game1, gameManager.getObserverManagerByGame(game1.getName())));

        assertNotNull(gameManager.getGameByName(game1.getName()));

    }

    @Test
    public void testGetNetworkManager() {
        assertNotNull(gameManager.getGameNetworkManager());
    }
}
