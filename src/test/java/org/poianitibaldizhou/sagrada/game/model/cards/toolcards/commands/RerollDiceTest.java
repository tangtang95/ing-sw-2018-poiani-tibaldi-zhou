package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RerollDiceTest {
    @Mock
    private Game game;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private Player invokerPlayer;

    private ICommand command;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new RerollDice();
    }

    @Test
    public void executeCommandTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        Dice dice = new Dice(1, Color.RED);
        when(executor.getNeededDice()).thenReturn(dice);
        CommandFlow commandFlow = command.executeCommand(invokerPlayer, executor, game);
        assertEquals(CommandFlow.MAIN, commandFlow);
        verify(executor, times(1)).getNeededDice();
        ArgumentCaptor<Dice> argumentCaptor = ArgumentCaptor.forClass(Dice.class);
        verify(executor, times(1)).setNeededDice(argumentCaptor.capture());
        assertEquals(dice.getColor(),argumentCaptor.getValue().getColor());
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RerollDice());
        assertNotEquals(command, new AddDiceToDiceBag());
    }
}
