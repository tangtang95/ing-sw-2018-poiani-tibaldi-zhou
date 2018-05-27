package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResetStateTest {
    @Mock
    private Game game;

    private ResetState resetState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        resetState = new ResetState(game);
        resetState.init();
        when(game.getNumberOfPlayers()).thenReturn(4);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        resetState = null;
    }

    @Test
    public void readyGameSuccess() throws Exception {
        resetState.readyGame("player1");
        resetState.readyGame("player2");
        resetState.readyGame("player3");
        resetState.readyGame("player4");
        verify(game).setState(ArgumentMatchers.any(SetupPlayerState.class));
    }

    @Test(expected = InvalidActionException.class)
    public void readyGameInvalid() throws Exception {
        resetState.readyGame("player2");
        resetState.readyGame("player1");
        resetState.readyGame("player1");
    }

}