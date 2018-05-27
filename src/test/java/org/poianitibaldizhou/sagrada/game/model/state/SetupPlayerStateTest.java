package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.observers.IGameObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.IStateObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Dependency class with:
 * - GameInjector
 * - SetupGameState constructor
 */
public class SetupPlayerStateTest {
    private SetupPlayerState setupPlayerState;

    @Mock
    private Game game;

    @Mock
    private IGameObserver player1Obs, player2Obs, player3Obs, player4Obs;

    @Mock
    private IStateObserver state1obs, state2obs, state3obs, state4obs;

    private String player1, player2, player3, player4;

    private List<String> playerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        player1 = "player1";
        player2 = "player2";
        player3 = "player3";
        player4 = "player4";
        playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        Map<String, IGameObserver> gameObserver = new HashMap<>();
        gameObserver.put(player1, player1Obs);
        gameObserver.put(player2, player2Obs);
        gameObserver.put(player3, player3Obs);
        gameObserver.put(player4, player4Obs);

        Map<String, IStateObserver> stateObservers = new HashMap<>();
        stateObservers.put(player1, state1obs);
        stateObservers.put(player2, state2obs);
        stateObservers.put(player3, state3obs);
        stateObservers.put(player4, state4obs);

        when(game.getNumberOfPlayers()).thenReturn(playerList.size());
        when(game.getUserToken()).thenReturn(playerList);
        when(game.getDiceBag()).thenReturn(new DrawableCollection<>());
        when(game.getState()).thenReturn(mock(SetupGameState.class));
        when(game.getNumberOfPrivateObjectiveCardForGame())
                .thenReturn(MultiPlayerGame.NUMBER_OF_PRIVATE_OBJECTIVE_CARDS);
        when(game.getGameObservers()).thenReturn(gameObserver);
        when(game.getStateObservers()).thenReturn(stateObservers);
        setupPlayerState = new SetupPlayerState(game);
        setupPlayerState.init();
    }

    @After
    public void tearDown() throws Exception {
        playerList = null;
        setupPlayerState = null;
        game = null;
    }

    @Test
    public void initTest() throws Exception {
        SetupPlayerState state = new SetupPlayerState(game);
        state.init();
        for (String token : playerList) {
            assertFalse(state.isPlayerReady(token));
            assertEquals("size of schemaCards", 2, state.getSchemaCardsOfPlayer(token).size());
        }
    }

    @Test
    public void readyCorrectTest() throws Exception {
        List<List<SchemaCard>> schemaCards1 = setupPlayerState.getSchemaCardsOfPlayer(player1);
        setupPlayerState.ready(player1, schemaCards1.get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player1));
        for (String player : playerList) {
            if (!player.equals(player1))
                assertFalse(setupPlayerState.isPlayerReady(player));
        }
    }

    @Test
    public void readyCorrectFlowTest() throws Exception {
        List<List<SchemaCard>> schemaCards1 = setupPlayerState.getSchemaCardsOfPlayer(player1);
        setupPlayerState.ready(player1, schemaCards1.get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player1));
        for (String player : playerList) {
            if (!player.equals(player1))
                assertFalse(setupPlayerState.isPlayerReady(player));
        }
        setupPlayerState.ready(player2, setupPlayerState.getSchemaCardsOfPlayer(player2).get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player2));
        for (String player : playerList) {
            if (!player.equals(player1) && !player.equals(player2))
                assertFalse(setupPlayerState.isPlayerReady(player));
        }
        setupPlayerState.ready(player3, setupPlayerState.getSchemaCardsOfPlayer(player3).get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player3));
        assertFalse(setupPlayerState.isPlayerReady(player4));
        setupPlayerState.ready(player4, setupPlayerState.getSchemaCardsOfPlayer(player4).get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player4));
        verify(game).setState(ArgumentMatchers.any(SetupGameState.class));
    }

    @Test(expected = InvalidActionException.class)
    public void readyExceptionWhenAlreadyReadiedTest() throws Exception {
        List<List<SchemaCard>> schemaCards1 = setupPlayerState.getSchemaCardsOfPlayer(player1);
        setupPlayerState.ready(player1, schemaCards1.get(0).get(0));
        assertTrue(setupPlayerState.isPlayerReady(player1));
        for (String player : playerList) {
            if (!player.equals(player1))
                assertFalse(setupPlayerState.isPlayerReady(player));
        }
        setupPlayerState.ready(player1, schemaCards1.get(1).get(0));
    }

    @Test(expected = InvalidActionException.class)
    public void readyExceptionWhenSchemaCardIsDifferentTest() throws Exception {
        List<List<SchemaCard>> schemaCards2 = setupPlayerState.getSchemaCardsOfPlayer(player2);
        setupPlayerState.ready(player1, schemaCards2.get(0).get(0));
    }


    @Test
    public void containsSchemaCard() throws Exception {
        List<List<SchemaCard>> schemaCards = setupPlayerState.getSchemaCardsOfPlayer(player1);
        assertTrue(setupPlayerState.containsSchemaCard(player1, schemaCards.get(0).get(0)));
    }

}