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
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class CheckTurnTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;
    @Mock
    private TurnState turnState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(game.getState()).thenReturn(turnState);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        game = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommand() throws Exception {
        command = new CheckTurn(1);
        when(turnState.isFirstTurn()).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));

        when(turnState.isFirstTurn()).thenReturn(false);
        try{
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

        command = new CheckTurn(2);
        when(turnState.isFirstTurn()).thenReturn(true);
        try{
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

        when(turnState.isFirstTurn()).thenReturn(false);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
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