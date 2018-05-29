package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.ToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.observers.realobservers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IfContinueTest {

    @Mock
    private Player player;

    @Mock
    private ToolCardExecutor toolCardExecutor;

    @Mock
    private TurnState turnState;

    private IfContinue command;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new IfContinue();
        List<ToolCardExecutorFakeObserver> observerList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            observerList.add(mock(ToolCardExecutorFakeObserver.class));
        }
        when(toolCardExecutor.getObservers()).thenReturn(observerList);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        player = null;
        toolCardExecutor = null;
        turnState = null;
    }

    @Test
    public void executeCommandTestContinue() throws Exception {
        when(toolCardExecutor.getNeededAnswer()).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, toolCardExecutor, turnState));
        for (IToolCardExecutorObserver obs: toolCardExecutor.getObservers()) {
            verify(obs).notifyNeedContinueAnswer();
        }
    }

    @Test
    public void equalsTest() throws Exception {
        ICommand newCommand = new IfContinue();
        assertTrue(command.equals(newCommand));
        assertFalse(command.equals(new Object()));
    }

    @Test
    public void hashCodeTest() throws Exception {
        ICommand newCommand = new IfContinue();
        assertEquals(command.hashCode(), newCommand.hashCode());
        assertNotEquals(command.hashCode(), new Object().hashCode());
    }

}