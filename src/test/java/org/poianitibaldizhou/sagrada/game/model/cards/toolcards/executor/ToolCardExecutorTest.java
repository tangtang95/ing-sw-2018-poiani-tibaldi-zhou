package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.board.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ClearAll;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ModifyDiceValue;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ToolCardExecutorTest {

    @Mock
    private Game game;
    @Mock
    private Player player;
    @Mock
    private TurnState state;

    private ToolCardExecutor executor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Node<ICommand> root = new Node<>(mock(ICommand.class));
        executor = spy(new ToolCardExecutor(game, player, state));
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        player = null;
        executor = null;
    }

    @Test
    public void observerTest() throws Exception {

    }

    // TODO fix this test 'cause they run in infinite loop

    @Test
    public void neededValueTest() throws Exception {
        int value = 0;
        Thread thread = new Thread(() -> {
            try {
                int temp = executor.getNeededValue();
                assertEquals(value, temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setNeededValue(value);
        thread.join();
        assertFalse(thread.isAlive());
    }

    @Test
    public void neededDiceTest() throws Exception {
        Dice dice = mock(Dice.class);
        Thread thread = new Thread(() -> {
            try {
                Dice temp = executor.getNeededDice();
                assertEquals(dice, temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setNeededDice(dice);
        thread.join();
        assertFalse(thread.isAlive());
    }

    @Test
    public void neededColorTest() throws Exception {
        Color color = Color.BLUE;
        Thread thread = new Thread(() -> {
            try {
                Color temp = executor.getNeededColor();
                assertEquals(color, temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setNeededColor(color);
        thread.join();
        assertFalse(thread.isAlive());
    }

    @Test
    public void positionTest() throws Exception {
        Position position = mock(Position.class);
        Thread thread = new Thread(() -> {
            try {
                Position temp = executor.getNeededPosition();
                assertEquals(position, temp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setNeededPosition(position);
        thread.join();
        assertFalse(thread.isAlive());
    }

    @Test
    public void waitForTurnEndTest() throws Exception {
        Thread thread = new Thread(() -> {
            try {
                executor.waitForTurnEnd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setTurnEnded(true);
        thread.join();
        assertFalse(thread.isAlive());
        verify(executor).waitForTurnEnd();
    }

    @Test
    public void waitForToolCardExecutionEndTest() throws Exception {
        executor.setIsExecutingCommands(true);
        Thread thread = new Thread(() -> {
            try {
                executor.waitToolCardExecutionEnd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setIsExecutingCommands(false);
        thread.join();
        assertFalse(thread.isAlive());
        verify(executor).waitToolCardExecutionEnd();
    }

    @Test
    public void stopToolCardExecutionTest() throws Exception{
        when(game.getDraftPool()).thenReturn(mock(DraftPool.class));
        when(game.getRoundTrack()).thenReturn(mock(RoundTrack.class));
        when(game.getDiceBag()).thenReturn(mock(DrawableCollection.class));
        when(player.getSchemaCard()).thenReturn(mock(SchemaCard.class));
        when(state.getSkipTurnPlayers()).thenReturn(new HashMap<>());

        Node<ICommand> commandNode = new Node<>(new ModifyDiceValue());
        executor = new ToolCardExecutor(game, player, state);
        executor.setPreCommands(new Node<>(new ClearAll()));
        executor.setCoreCommands(commandNode);
        // TODO
    }



    @Test
    public void invokeNullCommandsTest() throws Exception{
        // TODO
    }

}