package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RerollDraftPoolTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private Game game;

    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        command  = new RerollDraftPool();
    }

    @After
    public void tearDown() {
        game = null;
        invokerPlayer = null;
        executor = null;
        command = null;
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        CommandFlow commandFlow = command.executeCommand(invokerPlayer, executor, game);
        assertEquals(CommandFlow.MAIN, commandFlow);
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RerollDraftPool());
        assertNotEquals(command, new SkipTurn(1));
    }
}
