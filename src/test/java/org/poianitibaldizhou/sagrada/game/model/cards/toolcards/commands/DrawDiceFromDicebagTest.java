package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DrawDiceFromDicebagTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new DrawDiceFromDicebag();
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
        Dice dice = mock(Dice.class);
        when(game.getDiceFromDiceBag()).thenReturn(dice);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
        verify(game).getDiceFromDiceBag();
        verify(executor).setNeededDice(dice);
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new DrawDiceFromDicebag(), command);
        assertNotEquals(new Object(), command);
    }

}