package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test with dependency class:
 * -
 */
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
        game = null;
        player1 = null;
        player2 = null;
        player3 = null;
        player4 = null;
    }

    @Test
    public void chooseActionTest() throws Exception{
        //TODO
    }

    @Test
    public void useCardTest() throws Exception {
        //TODO
    }

    @Test
    public void placeDiceTest() throws Exception {
        //TODO
    }


    @Test
    public void testNextTurnWhenIsFirstTurn() throws Exception {
        TurnState turnState = new TurnState(game, 0,player1, player1, true);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        turnState.chooseAction(player1, new EndTurnAction());
        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
        assertEquals("player incorrect", player2, argument.getValue().getCurrentTurnPlayer());
    }

    @Test
    public void testNextTurnWhenIsFirstTurnButLastPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player2, player1, true);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        turnState.chooseAction(player1, new EndTurnAction());
        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
        assertEquals("player incorrect", player1, argument.getValue().getCurrentTurnPlayer());
        assertFalse(argument.getValue().isFirstTurn());
    }

    @Test
    public void testNextTurnWhenIsNotFirstTurnAndCurrentPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player1, player1, false);
        when(game.getNextIndexOfPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);

        turnState.chooseAction(player1, new EndTurnAction());
        verify(game).setState(ArgumentMatchers.any(RoundEndState.class));
    }

    @Test
    public void getCurrentPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        assertEquals(player1, turnState.getCurrentRoundPlayer());
        assertNotEquals(player2, turnState.getCurrentRoundPlayer());
    }


}