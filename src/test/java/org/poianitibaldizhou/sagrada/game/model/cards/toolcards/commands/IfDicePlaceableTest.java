package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IfDicePlaceableTest {
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
        command = new IfDicePlaceable();
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
        Position position = mock(Position.class);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getPosition()).thenReturn(position);
        when(invokerPlayer.isDicePositionableOnSchemaCard(dice, position.getRow(), position.getColumn())).thenReturn(false);
        assertEquals(CommandFlow.SUB, command.executeCommand(invokerPlayer, executor, game));

        when(invokerPlayer.isDicePositionableOnSchemaCard(dice, position.getRow(), position.getColumn())).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new IfDicePlaceable(), command);
        assertNotEquals(new Object(), command);
    }

}