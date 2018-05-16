package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ClearCommandsTest {

    @Mock
    public Game game;

    @Mock
    public Player player;

    @Mock
    private ToolCardExecutor executor;

    private ICommand command;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = null;
    }

    @Test
    public void clearAll() throws InterruptedException, RemoteException, ExecutionCommandException {
        command = new ClearAll();
        assertEquals(command, new ClearAll());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor, game));;
        verify(executor, times(1)).setTurnEnded(false);
        verify(executor, times(1)).setNeededPosition(null);
        verify(executor, times(1)).setNeededDice(null);
        verify(executor, times(1)).setNeededValue(null);
        verify(executor, times(1)).setNeededColor(null);
    }

    @Test
    public void clearColor() throws InterruptedException, RemoteException, ExecutionCommandException {
        command = new ClearColor();
        assertEquals(command, new ClearColor());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor, game));;
        verify(executor, times(1)).setNeededColor(null);
    }

    @Test
    public void clearValue() throws InterruptedException, RemoteException, ExecutionCommandException {
        command = new ClearValue();
        assertEquals(command, new ClearValue());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor, game));;
        verify(executor, times(1)).setNeededValue(null);
    }

    @Test
    public void clearPosition() throws InterruptedException, RemoteException, ExecutionCommandException {
        command = new ClearPosition();
        assertEquals(command, new ClearPosition());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor, game));;
        verify(executor, times(1)).setNeededPosition(null);
    }

    @Test
    public void clearTurnEndCondition() throws InterruptedException, RemoteException, ExecutionCommandException {
        command = new ClearTurnEndCondition();
        assertEquals(command, new ClearTurnEndCondition());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor, game));;
        verify(executor, times(1)).setTurnEnded(false);
    }
}
