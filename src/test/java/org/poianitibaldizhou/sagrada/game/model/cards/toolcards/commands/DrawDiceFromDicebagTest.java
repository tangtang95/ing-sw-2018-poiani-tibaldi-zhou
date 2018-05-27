package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DrawDiceFromDicebagTest {
    private ICommand command;

    @Mock
    private DrawableCollection<Dice> diceBag;
    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState state;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new DrawDiceFromDicebag();
        when(executor.getTemporaryDicebag()).thenReturn(diceBag);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        diceBag = null;
        executor = null;
        state = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommand() throws Exception {
        Dice dice = mock(Dice.class);
        when(executor.getTemporaryDicebag().draw()).thenReturn(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, state));
        verify(executor, times(1)).setNeededDice(dice);
    }

    @Test
    public void executeCommandDrawFromEmptyDicebag() throws Exception {
        when(executor.getTemporaryDicebag().draw()).thenThrow(EmptyCollectionException.class);
        assertEquals(CommandFlow.EMPTY_DICEBAG, command.executeCommand(invokerPlayer, executor, state));
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new DrawDiceFromDicebag(), command);
        assertNotEquals(new Object(), command);
    }

}