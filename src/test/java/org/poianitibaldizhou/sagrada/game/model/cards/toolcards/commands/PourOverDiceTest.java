package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class PourOverDiceTest {

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState state;
    @Mock
    private Player invokerPlayer;

    @DataPoint
    private ICommand command;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        command = new PourOverDice();
    }

    @After
    public void tearDown() {
        command = null;
        state = null;
        invokerPlayer = null;
        executor = null;
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, state));
        verify(executor, times(1)).setNeededDice(dice.pourOverDice());
    }

    @Test
    public void equalsTest() {
        assert(command.equals(new PourOverDice()));
        assertNotEquals(command, new AddDiceToDiceBagTest());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new PourOverDice().hashCode(), new PourOverDice().hashCode());
        assertNotEquals(new PourOverDice().hashCode(), new ReRollDice().hashCode());
    }
}
