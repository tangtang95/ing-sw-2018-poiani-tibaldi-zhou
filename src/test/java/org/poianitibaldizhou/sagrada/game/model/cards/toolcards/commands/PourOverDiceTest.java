package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class PourOverDiceTest {

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
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
        game = null;
        invokerPlayer = null;
        executor = null;
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
        command.executeCommand(invokerPlayer, executor, game);
        verify(executor, times(1)).setNeededDice(dice.pourOverDice());
    }

    @Test
    public void equalsTest() {
        assert(command.equals(new PourOverDice()));
        assertNotEquals(command, new AddDiceToDiceBagTest());
    }
}
