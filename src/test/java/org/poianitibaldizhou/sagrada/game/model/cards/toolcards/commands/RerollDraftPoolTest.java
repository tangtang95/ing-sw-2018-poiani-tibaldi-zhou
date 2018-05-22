package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RerollDraftPoolTest {
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
        command  = new RerollDraftPool();
        when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() {
        stateGame = null;
        invokerPlayer = null;
        executor = null;
        command = null;
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RerollDraftPool());
        assertNotEquals(command, new SkipTurn(1));
    }
}
