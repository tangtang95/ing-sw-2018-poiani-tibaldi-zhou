package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ModifyDiceValueTest {
    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;
    @Mock
    private DraftPool draftPool;
    @Mock
    private IToolCardExecutorObserver observer1, observer2, observer3;

    private ICommand command;
    private List<IToolCardExecutorObserver> observerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(executor.getObservers()).thenReturn(observerList);
        when(game.getDraftPool()).thenReturn(draftPool);
        command = new ModifyDiceValue();
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        game = null;
        invokerPlayer = null;
        draftPool = null;
        observerList = null;
    }

    @Test
    public void executeCommand() throws Exception {
        Dice dice = mock(Dice.class);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getNeededValue()).thenReturn(7);
        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        } catch (ExecutionCommandException e) {
            assertNotEquals(null, e);
        }
        when(executor.getNeededValue()).thenReturn(0);
        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        } catch (ExecutionCommandException e) {
            assertNotEquals(null, e);
        }
        when(executor.getNeededValue()).thenReturn(6);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
        when(executor.getNeededValue()).thenReturn(1);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));

        for (IToolCardExecutorObserver obs: observerList) {
            verify(obs, times(4)).notifyNeedNewValue();
        }
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new ModifyDiceValue(), command);
        assertNotEquals(new Object(), command);
    }

}