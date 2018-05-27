package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RemoveFavorTokenTest {
    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private TurnState stateGame;
    @Mock
    private Player invokerPlayer;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new RemoveFavorToken(4);
    }

    @After
    public void tearDown() throws Exception {
        stateGame = null;
        invokerPlayer = null;
        executor = null;
        command = null;
    }

    @Test
    public void executeCommandTest() throws Exception {
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
        verify(invokerPlayer).removeCoins(((RemoveFavorToken) command).getNumberOfTokenToRemove());
    }

    @Test
    public void equalsTest() throws Exception {
        //Different Cost
        ICommand newCommand = new RemoveFavorToken(3);
        assertFalse(command.equals(newCommand));

        //Different Class
        assertFalse(command.equals(new Object()));

        //Same cost
        newCommand = new RemoveFavorToken(4);
        assertTrue(command.equals(newCommand));
    }

    @Test
    public void hashCodeTest() throws Exception {
        //Different Cost
        ICommand newCommand = new RemoveFavorToken(3);
        assertNotEquals(command.hashCode(), newCommand.hashCode());

        //Different Class
        assertNotEquals(command.hashCode(), (new ModifyDiceValueByDelta(3)).hashCode());

        //Same cost
        newCommand = new RemoveFavorToken(4);
        assertEquals(command.hashCode(), newCommand.hashCode());
    }

}