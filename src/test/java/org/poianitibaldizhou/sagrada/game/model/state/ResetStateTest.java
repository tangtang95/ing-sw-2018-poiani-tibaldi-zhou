package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.network.observers.fakeobservers.StateFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IStateFakeObserver;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResetStateTest {
    @Mock
    private Game game;

    @Mock
    private StateFakeObserver gameFakeObserver1, gameFakeObserver2, gameFakeObserver3;

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

    @Test
    public void forceGameTerminationBeforeStarting() {
        Map<String, IStateFakeObserver> iGameObserverMap = new HashMap<>();
        iGameObserverMap.put("test1", gameFakeObserver1);
        iGameObserverMap.put("test2", gameFakeObserver2);
        iGameObserverMap.put("test3", gameFakeObserver3);
        when(game.getStateObservers()).thenReturn(iGameObserverMap);
        resetState.forceGameTerminationBeforeStarting();
        verify(gameFakeObserver1).onGameTerminationBeforeStarting();
        verify(gameFakeObserver2).onGameTerminationBeforeStarting();
        verify(gameFakeObserver3).onGameTerminationBeforeStarting();

    }
}