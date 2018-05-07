package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SetupPlayerStateTest {
    private SetupPlayerState setupPlayerState;

    @Mock
    private Game game;

    @Mock
    private Player player1, player2, player3, player4;

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
        setupPlayerState = new SetupPlayerState(game);
    }

    @After
    public void tearDown() throws Exception {
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
        List<SchemaCard> schemaCards = setupPlayerState.getSchemaCardsOfPlayer(player1);
        assertTrue(setupPlayerState.ready(player1, schemaCards.get(0)));
        assertTrue(setupPlayerState.isPlayerReady(player1));
        for (Player player : playerList) {
            if (player != player1)
                assertFalse(setupPlayerState.isPlayerReady(player));
        }

    }

    @Test
    public void containsSchemaCard() throws Exception {
        List<SchemaCard> schemaCards = setupPlayerState.getSchemaCardsOfPlayer(player1);
        assertTrue(setupPlayerState.containsSchemaCard(player1, schemaCards.get(0)));
    }

}