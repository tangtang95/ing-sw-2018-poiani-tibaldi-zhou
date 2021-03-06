package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

public class ReRollDraftPoolTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private TurnState stateGame;

    @Mock
    private Player invokerPlayer;

    @Mock
    private DraftPool draftPool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        command  = new ReRollDraftPool();
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() {
        stateGame = null;
        invokerPlayer = null;
        executor = null;
        command = null;
    }

    @Test
    public void executeCommandTest() throws Exception {
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new ReRollDraftPool());
        assertNotEquals(command, new SkipTurn(1));
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new ReRollDraftPool().hashCode(), new ReRollDraftPool().hashCode());
        assertNotEquals(new ReRollDraftPool().hashCode(), new ReRollDice().hashCode());
    }
}
