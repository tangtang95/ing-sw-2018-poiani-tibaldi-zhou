package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class PayDiceTest {
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
        command = new PayDice(Color.BLUE);
        dice = new Dice(2, Color.BLUE);
        draftPool = new DraftPool();
        draftPool.addDice(new Dice(5, Color.RED));
        draftPool.addDice(new Dice(2, Color.YELLOW));
        draftPool.addDice(dice);
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() throws Exception {
        stateGame = null;
        dice = null;
        draftPool = null;
        invokerPlayer = null;
        executor = null;
        command = null;
    }


    @Test
    public void executeCommandSuccess() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        DraftPool newDraftPool = new DraftPool();
        newDraftPool.addDices(draftPool.getDices());
        newDraftPool.useDice(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer,executor,stateGame));
        assertEquals(newDraftPool, executor.getTemporaryDraftPool());
    }

    @Test
    public void executeCommandDiceColorDifferent() throws Exception {
        when(executor.getNeededDice()).thenReturn(new Dice(5, Color.RED));
        assertEquals(CommandFlow.REPEAT, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test
    public void executeCommandTestFailNoDiceInDraftPool() throws Exception{
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        when(executor.getNeededDice()).thenReturn(new Dice(dice.getNumber()-1, dice.getColor()));
        assertEquals(CommandFlow.NOT_DICE_IN_DRAFTPOOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void executeCommandTestFailEmptyDraftPool() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDraftPool()).thenReturn(new DraftPool());
        assertEquals(CommandFlow.NOT_DICE_IN_DRAFTPOOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void equalsTest() throws Exception {
        ICommand newCommand = new PayDice(Color.BLUE);
        assertTrue(newCommand.equals(command));

        newCommand = new PayDice(Color.RED);
        assertFalse(command.equals(newCommand));

        assertFalse(command.equals(new Object()));
    }

    @Test
    public void hashCodeTest() throws Exception {
        ICommand newCommand = new PayDice(Color.BLUE);
        assertEquals(newCommand.hashCode(), command.hashCode());

        newCommand = new PayDice(Color.RED);
        assertNotEquals(newCommand.hashCode(), command.hashCode());

        assertNotEquals(newCommand.hashCode(), command.hashCode());
    }
}