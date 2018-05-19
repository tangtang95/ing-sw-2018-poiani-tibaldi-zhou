package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SkipTurnTest {

    @Mock
    private Player player;

    @Mock
    private Game game;

    @Mock
    private ToolCardExecutor executor;

    private ICommand skip1;
    private ICommand skip2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        skip1 = new SkipTurn(1);
        skip2 = new SkipTurn(2);
    }

    @After
    public void tearDown() {
        game = null;
        skip1 = null;
        skip2 = null;
        executor = null;
        player = null;
    }

    @Test
    public void testExecuteCommand() throws InterruptedException, RemoteException, ExecutionCommandException {
        TurnState turnState = mock(TurnState.class);
        when(game.getState()).thenReturn(turnState);
        assertEquals(CommandFlow.MAIN, skip1.executeCommand(player, executor, game));
        assertEquals(CommandFlow.MAIN, skip2.executeCommand(player, executor, game));
    }

    @Test
    public void equalsTest() {
        assertNotEquals(skip1, skip2);
        assertNotEquals(skip1, new RerollDice());
        assertNotEquals(skip2, new AddDiceToDiceBag());
        assertEquals(skip1, new SkipTurn(1));
    }

    @Test
    public void testConstructor() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(3);
        list.add(100);
        list.add(-1);
        
        for(Integer i : list) {
            try {
                ICommand temp = new SkipTurn(i);
                fail("Exception expected");
            } catch(IllegalArgumentException e) {
                
            }
        }
    }
}
