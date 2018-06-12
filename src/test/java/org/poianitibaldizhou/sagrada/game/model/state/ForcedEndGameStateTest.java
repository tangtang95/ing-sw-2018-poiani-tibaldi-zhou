package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ForcedEndGameStateTest {

    @Mock
    private Game game;

    @Mock
    private Player player;

    @DataPoint
    public static ForcedEndGameState forcedEndGameState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        forcedEndGameState = new ForcedEndGameState(game, player);
    }

    @Test
    public void init() {
        forcedEndGameState.init();
        verify(game).getStateObservers();
    }
}