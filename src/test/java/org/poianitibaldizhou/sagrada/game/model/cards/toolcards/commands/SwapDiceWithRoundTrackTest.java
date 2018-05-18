package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SwapDiceWithRoundTrackTest {

    @Mock
    private IToolCardExecutorObserver observer1;

    @Mock
    private IToolCardExecutorObserver observer2;

    @Mock
    private IToolCardExecutorObserver observer3;

    @Mock
    private Game game;

    @Mock
    private Player player;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private RoundTrack roundTrack;

    private ICommand command;
    private List<IToolCardExecutorObserver> observerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new SwapDiceWithRoundTrack();
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(game.getRoundTrack()).thenReturn(roundTrack);
        when(executor.getObservers()).thenReturn(observerList);
    }

    @After
    public void tearDown() throws Exception {
        observerList = null;
        game = null;
        executor = null;
        player = null;
        command = null;
    }

    @Test
    public void executeCommandWithSuccessTest() throws InterruptedException, RemoteException, ExecutionCommandException {
        assertEquals(CommandFlow.MAIN, command.executeCommand(player,executor,game));

        for(IToolCardExecutorObserver obs : observerList)
            verify(obs, times(1)).notifyNeedDiceFromRoundTrack(roundTrack);
    }

    @Test
    public void executeCommandFailTest() throws DiceNotFoundException, RemoteException, InterruptedException {
        Dice dice = new Dice(1, Color.BLUE);
        Dice roundTrackDice = new Dice(5, Color.YELLOW);
        doThrow(new DiceNotFoundException("")).when(game).swapDraftPoolDice(dice, roundTrackDice);
        when(executor.getNeededDice()).thenReturn(dice).thenReturn(roundTrackDice);

        try {
            command.executeCommand(player, executor, game);
            fail("Exception expected");
        } catch (ExecutionCommandException e) {

        }
    }

    @Test
    public void testEquals() {
        assertEquals(command, new SwapDiceWithRoundTrack());
        assertNotEquals(command, new RerollDice());
    }
}
