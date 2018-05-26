package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.util.ArrayList;
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
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);
        when(game.getNextPlayer(player2, Direction.CLOCKWISE)).thenReturn(player3);
        when(game.getNextPlayer(player3, Direction.CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player4, Direction.CLOCKWISE)).thenReturn(player1);
        when(game.getNextPlayer(player4, Direction.COUNTER_CLOCKWISE)).thenReturn(player3);
        when(game.getNextPlayer(player3, Direction.COUNTER_CLOCKWISE)).thenReturn(player2);
        when(game.getNextPlayer(player2, Direction.COUNTER_CLOCKWISE)).thenReturn(player1);
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);
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
        roundEndState.init();
        roundEndState.nextRound();
        verify(game).addRemainingDiceToRoundTrack(round);
        verify(game).clearDraftPool();
        verify(game).setState(ArgumentMatchers.any(RoundStartState.class));
    }

    @Test
    public void nextRoundWhenIsLastRound() throws Exception {
        int round = 9;
        roundEndState = new RoundEndState(game, round, player1);
        roundEndState.init();
        roundEndState.nextRound();
        verify(game).addRemainingDiceToRoundTrack(round);
        verify(game).clearDraftPool();
        verify(game).setState(ArgumentMatchers.any(EndGameState.class));
    }

    @Test
    public void getCurrentPlayer() throws Exception {
        roundEndState = new RoundEndState(game, 0, player1);
        roundEndState.init();
        assertEquals(player1, roundEndState.getCurrentRoundPlayer());
        assertNotEquals(player2, roundEndState.getCurrentRoundPlayer());
    }

}