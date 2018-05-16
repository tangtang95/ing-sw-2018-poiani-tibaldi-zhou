package org.poianitibaldizhou.sagrada.game.model.cards.toolcards;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ModifyDiceValue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ToolCardExecutorTest {

    @Mock
    private Game game;
    @Mock
    private Player player;

    private ToolCardExecutor executor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Node<ICommand> root = new Node<>(mock(ICommand.class));
        executor = spy(new ToolCardExecutor(root, player, game));

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
        Thread thread = new Thread(() -> {
            try {
                executor.waitForToolCardExecutionEnd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        assertTrue(thread.isAlive());
        executor.setIsDone(true);
        thread.join();
        assertFalse(thread.isAlive());
        verify(executor).waitForToolCardExecutionEnd();
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
    public void invokeCommandsTest() throws Exception{
        Node<ICommand> root = new Node(mock(ICommand.class));
        Node<ICommand> firstLeft = new Node(mock(ICommand.class));
        Node<ICommand> firstRight = new Node(mock(ICommand.class));
        Node<ICommand> firstLeftSecondLeft = new Node(mock(ICommand.class));
        Node<ICommand> firstLeftSecondLeftThirdLeft = new Node(mock(ICommand.class));

        root.setLeftChild(firstLeft);
        root.setRightChild(firstRight);
        firstLeft.setLeftChild(firstLeftSecondLeft);
        firstLeftSecondLeft.setLeftChild(firstLeftSecondLeftThirdLeft);

        


    }

    @Test
    public void newInstanceTest() throws Exception {
    }

}