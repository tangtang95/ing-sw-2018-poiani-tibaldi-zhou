package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
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
        assertEquals(CommandFlow.TURN_CHECK_FAILED, command.executeCommand(invokerPlayer, executor, stateGame));

        command = new CheckTurn(2);
        when(stateGame.isFirstTurn()).thenReturn(true);
        assertEquals(CommandFlow.TURN_CHECK_FAILED, command.executeCommand(invokerPlayer, executor, stateGame));

        when(stateGame.isFirstTurn()).thenReturn(false);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test(expected = Exception.class)
    public void testConstructorException() {
        new CheckTurn(-1);
    }

    @Test(expected = Exception.class)
    public void testConstructorException2() {
        new CheckTurn(3);
    }

    @Test
    public void testHashCode() {
        assertEquals(new CheckTurn(1).hashCode(), new CheckTurn(1).hashCode());
        assertNotEquals(new CheckTurn(1).hashCode(), new CheckTurn(2).hashCode());
        assertNotEquals(new CheckTurn(1), new AddDiceToDiceBag());
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
        assertNotEquals(command, new AddDiceToDiceBag());
    }

}