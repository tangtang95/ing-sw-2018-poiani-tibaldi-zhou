package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddDiceToDraftPoolTest {
    private ICommand command;
    private DraftPool draftPool;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private IStateGame stateGame;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new AddDiceToDraftPool();
        draftPool = new DraftPool();
        draftPool.addDice(new Dice(1, Color.PURPLE));
        draftPool.addDice(new Dice(1,Color.RED));
        when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        stateGame = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommandTest() throws Exception {
        Dice dice = new Dice(1, Color.PURPLE);
        DraftPool tempDraftPool = new DraftPool();
        tempDraftPool.addDices(draftPool.getDices());
        tempDraftPool.addDice(dice);
        when(executor.getNeededDice()).thenReturn(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
        assertEquals(tempDraftPool, executor.getTemporaryDraftpool());
    }

    @Test
    public void equalsTest() throws Exception {
        assertEquals(new AddDiceToDraftPool(), command);
        assertNotEquals(new Object(), command);
    }
}