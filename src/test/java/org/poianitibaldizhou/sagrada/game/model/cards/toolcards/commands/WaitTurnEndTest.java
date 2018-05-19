package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WaitTurnEndTest {

    @Mock
    private Player player;

    @Mock
    private Game game;

    @Mock
    private ToolCardExecutor executor;

    private ICommand command;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new WaitTurnEnd();
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        game = null;
        executor = null;
        player = null;
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, game));
        verify(executor, times(1)).waitForTurnEnd();
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new WaitTurnEnd());
        assertNotEquals(command, new RemoveDiceFromDraftPool());
    }
}