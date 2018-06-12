package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckBeforeDiceChosenTest {

    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Player invokerPlayer;
    @Mock
    private TurnState turnState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new CheckBeforeDiceChosen();
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        turnState = null;
        invokerPlayer = null;
    }

    @Test
    public void executeCommand() throws Exception {
        when(turnState.hasActionUsed(ArgumentMatchers.any(PlaceDiceAction.class))).thenReturn(true);
        assertEquals(CommandFlow.DICE_ALREADY_PLACED, command.executeCommand(invokerPlayer, executor, turnState));

        when(turnState.hasActionUsed(ArgumentMatchers.any(PlaceDiceAction.class))).thenReturn(false);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, turnState));
    }

    @Test
    public void equals() throws Exception {
        assertEquals(new CheckBeforeDiceChosen(), command);
        assertNotEquals(new Object(), command);
    }

    @Test
    public void testHashCode() {
        assertEquals(new CheckBeforeDiceChosen().hashCode(), new CheckBeforeDiceChosen().hashCode());
        assertNotEquals(new CheckBeforeDiceChosen().hashCode(), new AddDiceToDiceBag().hashCode());
    }

}