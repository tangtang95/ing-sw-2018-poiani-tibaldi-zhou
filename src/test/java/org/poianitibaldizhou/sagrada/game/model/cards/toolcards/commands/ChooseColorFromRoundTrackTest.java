package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChooseColorFromRoundTrackTest {

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;
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
        command = new ChooseColorFromRoundTrack();
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        game = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommand() throws Exception {
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
        for (IToolCardExecutorObserver obs: observerList) {
            verify(obs).notifyNeedColor();
        }
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new ChooseColorFromRoundTrack(), command);
        assertNotEquals(new Object(), command);
    }

}