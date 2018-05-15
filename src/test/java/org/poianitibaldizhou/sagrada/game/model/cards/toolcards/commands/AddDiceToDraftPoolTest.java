package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddDiceToDraftPoolTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executorHelper;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new AddDiceToDraftPool();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void executeCommandTest() throws Exception {
        Dice dice = mock(Dice.class);
        when(executorHelper.getNeededDice()).thenReturn(dice);
        assertTrue(command.executeCommand(invokerPlayer, executorHelper, game));
        verify(game).addDiceToDraftPool(dice);
    }

    @Test
    public void equalsTest() throws Exception {
        assertEquals(new AddDiceToDraftPool(), command);
        assertNotEquals(new Object(), command);
    }
}