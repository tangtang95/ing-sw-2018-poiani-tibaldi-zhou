package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.omg.CORBA.CODESET_INCOMPATIBLE;
import org.poianitibaldizhou.sagrada.exception.DiceNotFoundException;
import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;
import sun.text.resources.ro.CollationData_ro;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class RemoveDiceFromDraftPoolTest {

    private ICommand command;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;

    private Dice dice;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new RemoveDiceFromDraftPool();
        dice = new Dice(1, Color.BLUE);
        when(executor.getNeededDice()).thenReturn(dice);
    }

    @Test
    public void executeCommandTestSuccess() throws InterruptedException, RemoteException, ExecutionCommandException {
        CommandFlow commandFlow = command.executeCommand(invokerPlayer,executor,game);
        assertEquals(CommandFlow.MAIN, commandFlow);
    }

    @Test
    public void executeCommandTestFail() throws RemoteException, InterruptedException, DiceNotFoundException, EmptyCollectionException {
        doThrow(new DiceNotFoundException("")).when(game).useDraftPoolDice(dice);
        try {
            command.executeCommand(invokerPlayer,executor,game);
            fail("Exception expected");
        } catch (ExecutionCommandException e) {
        }
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new RemoveDiceFromDraftPool());
        assertNotEquals(command, new DrawDiceFromDicebag());
    }
}
