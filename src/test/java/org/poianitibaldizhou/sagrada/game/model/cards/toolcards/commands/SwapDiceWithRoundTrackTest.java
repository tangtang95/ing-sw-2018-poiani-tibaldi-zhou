package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SwapDiceWithRoundTrackTest {

    @Mock
    private IToolCardExecutorFakeObserver observer1, observer2, observer3;

    @Mock
    private TurnState stateGame;

    @Mock
    private Player player;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private RoundTrack roundTrack;

    @Mock
    private DraftPool draftPool;


    private ICommand command;
    private List<IToolCardExecutorFakeObserver> observerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new SwapDiceWithRoundTrack();

        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);

        when(executor.getTemporaryRoundTrack()).thenReturn(roundTrack);
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        when(executor.getObservers()).thenReturn(observerList);
    }

    @After
    public void tearDown() throws Exception {
        observerList = null;
        stateGame = null;
        executor = null;
        player = null;
        command = null;
    }

    @Test
    public void executeCommandWithSuccessTest() throws Exception {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(player, executor, stateGame));

        for (IToolCardExecutorFakeObserver obs : observerList)
            verify(obs, times(1)).notifyNeedDiceFromRoundTrack(roundTrack);
    }

    @Test
    public void executeCommandFailTest() throws Exception {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
        doThrow(new DiceNotFoundException("")).when(draftPool).useDice(dice);
        assertEquals(CommandFlow.REPEAT, command.executeCommand(player, executor, stateGame));
    }

    @Test
    public void executeCommandEmptyDraftPool() throws Exception {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
        doThrow(new EmptyCollectionException()).when(draftPool).useDice(dice);
        assertEquals(CommandFlow.EMPTY_DRAFT_POOL, command.executeCommand(player, executor, stateGame));
    }

    @Test
    public void executeCommandEmptyRoundTrack() throws Exception {
        RoundTrack roundTrack = new RoundTrack();
        when(executor.getTemporaryRoundTrack()).thenReturn(roundTrack);
        assertEquals(CommandFlow.EMPTY_ROUND_TRACK, command.executeCommand(player, executor, stateGame));
    }

    @Test
    public void testEquals() {
        assertEquals(command, new SwapDiceWithRoundTrack());
        assertNotEquals(command, new ReRollDice());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(command.hashCode(), new SwapDiceWithRoundTrack().hashCode());
        assertNotEquals(command.hashCode(), new AddDiceToDraftPool().hashCode());
    }
}
