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

public class ModifyDiceValueByDeltaTest {
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
        command = new ModifyDiceValueByDelta(1);
        Dice dice = mock(Dice.class);
        when(dice.getNumber()).thenReturn(3);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getNeededValue()).thenReturn(5);
        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

        when(executor.getNeededValue()).thenReturn(1);
        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

        when(executor.getNeededValue()).thenReturn(2);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));

        when(executor.getNeededValue()).thenReturn(4);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));

        for (IToolCardExecutorObserver obs: observerList) {
            verify(obs, times(4)).notifyNeedNewDeltaForDice(dice.getNumber(), 1);
        }

        command = new ModifyDiceValueByDelta(2);
        when(dice.getNumber()).thenReturn(1);
        for (int i = Dice.MIN_VALUE; i <= Dice.MAX_VALUE; i++) {
            when(executor.getNeededValue()).thenReturn(i);
            if(i == 3){
                assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
            }
            else{
                try {
                    command.executeCommand(invokerPlayer, executor, game);
                    fail("exception expected");
                }catch (ExecutionCommandException e){
                    assertNotEquals(null, e);
                }

            }
        }

        for (IToolCardExecutorObserver obs: observerList) {
            verify(obs, times(6)).notifyNeedNewDeltaForDice(dice.getNumber(), 2);
        }
    }

    @Test
    public void equals() throws Exception {
        command = new ModifyDiceValueByDelta(1);
        assertEquals(new ModifyDiceValueByDelta(1), command);
        assertNotEquals(new ModifyDiceValueByDelta(2), command);
        try {
            new ModifyDiceValueByDelta(-1);
            fail("exception expected");
        }catch (IllegalArgumentException e){
            assertNotEquals(null, e);
        }
        assertNotEquals(new Object(), command);
    }

}