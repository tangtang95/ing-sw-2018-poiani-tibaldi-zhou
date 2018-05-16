package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import javafx.geometry.Pos;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckDicePositionableTest {
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
        command = new CheckDicePositionable();
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
        SchemaCard schemaCard = mock(SchemaCard.class);
        when(invokerPlayer.getSchemaCard()).thenReturn(schemaCard);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getPosition()).thenReturn(position);
        when(schemaCard.isDicePositionable(dice, position.getRow(), position.getColumn())).thenReturn(false);

        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

        when(schemaCard.isDicePositionable(dice, position.getRow(), position.getColumn())).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, game));
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new CheckDicePositionable(), command);
        assertNotEquals(new Object(), command);
    }

}