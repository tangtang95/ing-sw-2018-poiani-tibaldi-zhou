package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooseDiceTest {
    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState state;
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
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        command = new ChooseDice();
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        state = null;
        invokerPlayer = null;
        draftPool = null;
        observerList = null;
    }

    @Test
    public void executeCommand() throws Exception {
        Dice d1 = mock(Dice.class);
        List<Dice> dices = new ArrayList<>();
        dices.add(d1);
        when(draftPool.getDices()).thenReturn(dices);
        command.executeCommand(invokerPlayer, executor, state);
        for (IToolCardExecutorObserver observer: observerList) {
            verify(observer).notifyNeedDice(draftPool.getDices());
        }
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new ChooseDice(), command);
        assertNotEquals(new Object(), command);
    }

}