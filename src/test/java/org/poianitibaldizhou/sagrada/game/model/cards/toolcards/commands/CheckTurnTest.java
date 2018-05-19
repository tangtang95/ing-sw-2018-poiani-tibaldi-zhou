package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CheckTurnTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState stateGame;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        stateGame = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommand() throws Exception {
        command = new CheckTurn(1);
        when(stateGame.isFirstTurn()).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));

        when(stateGame.isFirstTurn()).thenReturn(false);
        command.executeCommand(invokerPlayer, executor, stateGame);
        assertEquals(CommandFlow.STOP, command.executeCommand(invokerPlayer, executor, stateGame));

        command = new CheckTurn(2);
        when(stateGame.isFirstTurn()).thenReturn(true);
        assertEquals(CommandFlow.STOP, command.executeCommand(invokerPlayer, executor, stateGame));

        when(stateGame.isFirstTurn()).thenReturn(false);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test
    public void equals() throws Exception {
        command = new CheckTurn(1);
        assertEquals(new CheckTurn(1), command);
        assertNotEquals(new CheckTurn(2), command);
        command = new CheckTurn(2);
        assertEquals(new CheckTurn(2), command);
        assertNotEquals(new CheckTurn(1), command);
        assertNotEquals(new Object(), command);
    }

}