package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

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
    private TurnState state;
    @Mock
    private SchemaCard schemaCard;
    @Mock
    private Dice dice;
    @Mock
    private Player invokerPlayer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new IfDicePlaceable();
        when(executor.getTemporarySchemaCard()).thenReturn(schemaCard);
        when(executor.getNeededDice()).thenReturn(dice);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        state = null;
    }

    @Test
    public void executeCommand() throws Exception {
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, state));
    }

    @Test
    public void executeCommandFailing() throws Exception {
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)).thenReturn(false);
        assertEquals(CommandFlow.SUB, command.executeCommand(invokerPlayer, executor, state));
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new IfDicePlaceable(), command);
        assertNotEquals(new Object(), command);
    }

    @Test
    public void testHashCode() {
        assertEquals(new IfDicePlaceable().hashCode(), command.hashCode());
        assertNotEquals(command.hashCode(), new AddDiceToDiceBag().hashCode());
    }

}