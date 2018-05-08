package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetupPlayerStateTest {
    private SetupPlayerState setupPlayerState;

    @Mock
    private Game game;

    @Mock
    private static Player player1, player2, player3, player4;

    private List<Player> playerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getNumberOfPlayers()).thenReturn(playerList.size());
        when(game.getDiceBag()).thenReturn(new DrawableCollection<>());
        when(game.getState()).thenReturn(mock(SetupGameState.class));
        setupPlayerState = new SetupPlayerState(game);
    }

    @After
    public void tearDown() throws Exception {
        playerList = null;
        setupPlayerState = null;
        game = null;
    }

    @Test
    public void testConstructor() {
        SetupPlayerState state = new SetupPlayerState(game);
        for (Player player : playerList) {
            assertFalse(state.isPlayerReady(player));
            assertEquals("size of schemaCards", 2, state.getSchemaCardsOfPlayer(player).size());
        }
    }

    @Test
    public void ready() throws Exception {
        List<SchemaCard> schemaCards1 = setupPlayerState.getSchemaCardsOfPlayer(player1);
        assertTrue(setupPlayerState.ready(player1, schemaCards1.get(0)));
        assertTrue(setupPlayerState.isPlayerReady(player1));
        for (Player player : playerList) {
            if (player != player1)
                assertFalse(setupPlayerState.isPlayerReady(player));
        }

        assertFalse(setupPlayerState.ready(player1, schemaCards1.get(1)));
        assertFalse(setupPlayerState.ready(player2, schemaCards1.get(0)));
        assertTrue(setupPlayerState.ready(player2, setupPlayerState.getSchemaCardsOfPlayer(player2).get(0)));
        assertTrue(setupPlayerState.isPlayerReady(player2));
        for (Player player : playerList) {
            if (player != player1 && player != player2)
                assertFalse(setupPlayerState.isPlayerReady(player));
        }
        assertTrue(setupPlayerState.ready(player3, setupPlayerState.getSchemaCardsOfPlayer(player3).get(0)));
        assertTrue(setupPlayerState.isPlayerReady(player3));
        assertFalse(setupPlayerState.isPlayerReady(player4));
        assertTrue(setupPlayerState.ready(player4, setupPlayerState.getSchemaCardsOfPlayer(player4).get(0)));
        assertTrue(setupPlayerState.isPlayerReady(player4));
        verify(game).setState(ArgumentMatchers.any(SetupGameState.class));
    }

    @Test
    public void containsSchemaCard() throws Exception {
        List<SchemaCard> schemaCards = setupPlayerState.getSchemaCardsOfPlayer(player1);
        assertTrue(setupPlayerState.containsSchemaCard(player1, schemaCards.get(0)));
    }

}