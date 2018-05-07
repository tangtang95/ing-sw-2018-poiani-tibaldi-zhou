package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TurnStateTest {

    @Mock
    private Game game;
    @Mock
    private Player player1, player2, player3, player4;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        when(game.getPlayers()).thenReturn(playerList);
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     *
     */
    @Test
    public void testNextTurnWhenIsFirstTurn() throws Exception {
        TurnState turnState = new TurnState(game, player1, player1, true);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        doAnswer(invocationOnMock -> {
            TurnState newTurnState = (TurnState) invocationOnMock.getArgument(0);
            assertEquals("player incorrect", player2, newTurnState.getPlayer());
            return null;
        }).when(game).setState(ArgumentMatchers.any(TurnState.class));

        turnState.nextTurn();
        verify(game).setState(ArgumentMatchers.any(TurnState.class));
    }

    @Test
    public void testNextTurnWhenIsFirstTurnButLastPlayer() throws Exception {
        TurnState turnState = new TurnState(game, player2, player1, true);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        doAnswer(invocationOnMock -> {
            TurnState newTurnState = (TurnState) invocationOnMock.getArgument(0);
            assertEquals("player incorrect", player1, newTurnState.getPlayer());
            assertFalse(newTurnState.isFirstTurn());
            return null;
        }).when(game).setState(ArgumentMatchers.any(TurnState.class));

        turnState.nextTurn();
        verify(game).setState(ArgumentMatchers.any(TurnState.class));
    }

    @Test
    public void testNextTurnWhenIsNotFirstTurnAndCurrentPlayer() throws Exception {
        TurnState turnState = new TurnState(game, player1, player1, false);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        turnState.nextTurn();
        verify(game).setState(ArgumentMatchers.any(RoundEndState.class));
    }





}