package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AnswerExecutorEventTest {
    @Mock
    private Game game;

    @Mock
    private Player player;

    @Mock
    private TurnState turnState;

    private ToolCardExecutor executor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        executor = new ToolCardExecutor(game, player, turnState);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        player = null;
        turnState = null;
        executor = null;
    }

    @Test
    public void setNeededValueTest() throws Exception {
        boolean answer = true;
        AnswerExecutorEvent answerExecutorEvent = new AnswerExecutorEvent(answer);
        answerExecutorEvent.setNeededValue(executor);
        assertEquals(answer, executor.getNeededAnswer());
    }
}