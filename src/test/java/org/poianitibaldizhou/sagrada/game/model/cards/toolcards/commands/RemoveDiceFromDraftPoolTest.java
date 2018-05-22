package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class RemoveDiceFromDraftPoolTest {

    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState stateGame;
    @Mock
    private Player invokerPlayer;

    private Dice dice;
    private DraftPool draftPool;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new RemoveDiceFromDraftPool();
        dice = new Dice(2, Color.BLUE);
        draftPool = new DraftPool();
        draftPool.addDice(new Dice(5, Color.RED));
        draftPool.addDice(new Dice(2, Color.YELLOW));
        draftPool.addDice(dice);
    }

    @Test
    public void executeCommandTestSuccess() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
        DraftPool newDraftPool = new DraftPool();
        newDraftPool.addDices(draftPool.getDices());
        newDraftPool.useDice(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer,executor,stateGame));
        assertEquals(newDraftPool, executor.getTemporaryDraftpool());
    }

    @Test
    public void executeCommandTestFailNoDiceInDraftPool() throws Exception{
        when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
        when(executor.getNeededDice()).thenReturn(new Dice(dice.getNumber()-1, dice.getColor()));
        assertEquals(CommandFlow.NOT_DICE_IN_DRAFTPOOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void executeCommandTestFailEmptyDraftPool() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDraftpool()).thenReturn(new DraftPool());
        assertEquals(CommandFlow.EMPTY_DRAFTPOOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RemoveDiceFromDraftPool());
        assertNotEquals(command, new DrawDiceFromDicebag());
    }
}
