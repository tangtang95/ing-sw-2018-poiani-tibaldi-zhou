package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

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
    public void executeCommandTestSuccess() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        DraftPool newDraftPool = new DraftPool();
        newDraftPool.addDices(draftPool.getDices());
        newDraftPool.useDice(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer,executor,stateGame));
        assertEquals(newDraftPool, executor.getTemporaryDraftPool());
    }

    @Test
    public void executeCommandTestFailNoDiceInDraftPool() throws Exception{
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        when(executor.getNeededDice()).thenReturn(new Dice(dice.getNumber()-1, dice.getColor()));
        assertEquals(CommandFlow.NOT_DICE_IN_DRAFT_POOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void executeCommandTestFailEmptyDraftPool() throws Exception {
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDraftPool()).thenReturn(new DraftPool());
        assertEquals(CommandFlow.EMPTY_DRAFT_POOL, command.executeCommand(invokerPlayer,executor, stateGame));
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RemoveDiceFromDraftPool());
        assertNotEquals(command, new DrawDiceFromDiceBag());
    }

    @Test
    public void testHashCode() {
        assertEquals(command.hashCode(), new RemoveDiceFromDraftPool().hashCode());
        assertNotEquals(command.hashCode(), new DrawDiceFromDiceBag().hashCode());
    }
}
