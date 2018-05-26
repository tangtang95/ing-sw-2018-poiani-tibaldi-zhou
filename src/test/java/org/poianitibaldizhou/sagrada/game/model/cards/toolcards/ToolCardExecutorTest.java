package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

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
    /*
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
                Position temp = executor.getPosition();
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
        Node<ICommand> commandNode = new Node<>(new ModifyDiceValue());
        executor = new ToolCardExecutor(commandNode, player, game);
        executor.start();
        assertTrue(executor.isAlive());
        executor.interruptCommandsInvocation();
        executor.join();
        assertFalse(executor.isAlive());
    }

    @Test
    public void invokeCommandsTest1() throws Exception{
        ICommand command = mock(ICommand.class);
        Node<ICommand> root = spy(new Node<>(command));
        Node<ICommand> firstLeft = spy(new Node<>(command));
        Node<ICommand> firstRight = spy(new Node<>(command));
        Node<ICommand> firstLeftSecondLeft = spy(new Node<>(command));
        Node<ICommand> firstLeftSecondLeftThirdLeft = spy(new Node(command));

        root.setLeftChild(firstLeft);
        root.setRightChild(firstRight);
        firstLeft.setLeftChild(firstLeftSecondLeft);
        firstLeftSecondLeft.setLeftChild(firstLeftSecondLeftThirdLeft);

        executor = new ToolCardExecutor(root, player, game);

        when(command.executeCommand(player, executor, state)).thenReturn(CommandFlow.MAIN);
        executor.start();
        executor.join();
        verify(root).getData();
        verify(firstLeft).getData();
        verify(firstLeftSecondLeft).getData();
        verify(firstLeftSecondLeftThirdLeft).getData();
        verify(firstRight, times(0)).getData();
        assertEquals(false, executor.isExecutingCommands());
    }

    @Test
    public void invokeCommandsTest2() throws Exception{
        ICommand command = mock(ICommand.class);
        Node<ICommand> root = spy(new Node<>(command));
        Node<ICommand> firstLeft = spy(new Node<>(command));
        Node<ICommand> firstRight = spy(new Node<>(command));
        Node<ICommand> firstLeftSecondLeft = spy(new Node<>(command));
        Node<ICommand> firstLeftSecondLeftThirdLeft = spy(new Node<>(command));

        root.setLeftChild(firstLeft);
        root.setRightChild(firstRight);
        firstLeft.setLeftChild(firstLeftSecondLeft);
        firstLeftSecondLeft.setLeftChild(firstLeftSecondLeftThirdLeft);

        executor = new ToolCardExecutor(root, player, game);
        when(command.executeCommand(player,executor, state)).thenReturn(CommandFlow.SUB);
        executor.start();
        executor.join();
        verify(root).getData();
        verify(firstLeft, times(0)).getData();
        verify(firstLeftSecondLeft, times(0)).getData();
        verify(firstLeftSecondLeftThirdLeft, times(0)).getData();
        verify(firstRight).getData();
        assertEquals(false, executor.isExecutingCommands());
    }

    public void invokeNullCommandsTest() throws Exception{
        // TODO
    }
    */
    @Test
    public void newInstanceTest() throws Exception {
        // TODO
    }

}