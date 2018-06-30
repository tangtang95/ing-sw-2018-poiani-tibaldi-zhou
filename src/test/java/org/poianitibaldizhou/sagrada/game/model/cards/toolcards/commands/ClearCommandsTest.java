package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ClearCommandsTest {

    @Mock
    public TurnState stateGame;

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
    public void clearAll() throws InterruptedException {
        command = new ClearAll();
        assertEquals(command, new ClearAll());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(command.hashCode(), new ClearAll().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));

        verify(executor, times(1)).setTurnEnded(false);
        verify(executor, times(1)).setNeededPosition(null);
        verify(executor, times(1)).setNeededDice(null);
        verify(executor, times(1)).setNeededValue(null);
        verify(executor, times(1)).setNeededColor(null);
    }

    @Test
    public void clearColor() throws InterruptedException {
        command = new ClearColor();
        assertEquals(command, new ClearColor());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));

        verify(executor, times(1)).setNeededColor(null);

        assertEquals(command.hashCode(), new ClearColor().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());
    }

    @Test
    public void clearValue() throws InterruptedException{
        command = new ClearValue();
        assertEquals(command, new ClearValue());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));

        verify(executor, times(1)).setNeededValue(null);

        assertEquals(command.hashCode(), new ClearValue().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());
    }

    @Test
    public void clearPosition() throws Exception{
        command = new ClearPosition();
        assertEquals(command, new ClearPosition());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));
        verify(executor, times(1)).setNeededPosition(null);
        assertEquals(command.hashCode(), new ClearPosition().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());
    }

    @Test
    public void clearTurnEndCondition() throws Exception {
        command = new ClearTurnEndCondition();
        assertEquals(command, new ClearTurnEndCondition());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));
        verify(executor, times(1)).setTurnEnded(false);
        assertEquals(command.hashCode(), new ClearTurnEndCondition().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());

        assertEquals(command.hashCode(), new ClearTurnEndCondition().hashCode());
        assertNotEquals(command.hashCode(), new ClearDice().hashCode());

    }

    @Test
    public void clearDice() throws Exception {
        command = new ClearDice();
        assertEquals(command, new ClearDice());
        assertNotEquals(command, new AddDiceToDiceBagTest());
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));
        verify(executor, times(1)).setNeededDice(null);
        assertEquals(command.hashCode(), new ClearDice().hashCode());
        assertNotEquals(command.hashCode(), new ClearAll().hashCode());

    }
}
