package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.poianitibaldizhou.sagrada.game.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoundEndStateTest {

    @Mock
    private Game game;
    @Mock
    private Player player1, player2, player3, player4;
    @Mock
    private RoundTrack roundTrack;
    @Mock
    private DraftPool draftPool;
    
    private RoundEndState roundEndState;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<Player> playerList = new ArrayList<>();
        List<Dice> diceList = new ArrayList<>();
        when(game.getDraftPool()).thenReturn(draftPool);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        when(game.getPlayers()).thenReturn(playerList);

        diceList.add(new Dice(4, Color.BLUE));
        diceList.add(new Dice(3, Color.RED));
        diceList.add(new Dice(1, Color.YELLOW));
        diceList.add(new Dice(5, Color.BLUE));
        when(draftPool.getDices()).thenReturn(diceList);
        when(game.getNextIndexOfPlayer(player1, Direction.CLOCKWISE)).thenReturn(1);
        when(game.getNextIndexOfPlayer(player2, Direction.CLOCKWISE)).thenReturn(2);
        when(game.getNextIndexOfPlayer(player3, Direction.CLOCKWISE)).thenReturn(3);
        when(game.getNextIndexOfPlayer(player4, Direction.CLOCKWISE)).thenReturn(0);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        player1 = null;
        player2 = null;
        player3 = null;
        player4 = null;
        roundTrack = null;
        draftPool = null;
        roundEndState = null;
    }

    @Test
    public void nextRoundWhenIsNotLastRound() throws Exception {
        int round = 0;
        roundEndState = new RoundEndState(game, round, player1);
        roundEndState.nextRound();
        verify(game).addRemainingDiceToRoundTrack(round);
        verify(game).clearDraftPool();
        verify(game).setState(ArgumentMatchers.any(RoundStartState.class));
    }

    @Test
    public void nextRoundWhenIsLastRound() throws Exception {
        int round = 9;
        roundEndState = new RoundEndState(game, round, player1);
        roundEndState.nextRound();
        verify(game).addRemainingDiceToRoundTrack(round);
        verify(game).clearDraftPool();
        verify(game).setState(ArgumentMatchers.any(EndGameState.class));
    }

    @Test
    public void getCurrentPlayer() throws Exception {
        roundEndState = new RoundEndState(game, 0, player1);
        assertEquals(player1, roundEndState.getCurrentRoundPlayer());
        assertNotEquals(player2, roundEndState.getCurrentRoundPlayer());
    }

}