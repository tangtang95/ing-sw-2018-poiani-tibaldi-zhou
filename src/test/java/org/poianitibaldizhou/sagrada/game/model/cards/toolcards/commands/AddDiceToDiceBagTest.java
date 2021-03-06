package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AddDiceToDiceBagTest {

    private ICommand command;
    private DrawableCollection<Dice> diceBag;

    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Player invokerPlayer;
    @Mock
    private TurnState state;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new AddDiceToDiceBag();

        diceBag = new DrawableCollection<>();
        diceBag.addElement(new Dice(5, Color.PURPLE));
        diceBag.addElement(new Dice(5, Color.RED));
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        invokerPlayer = null;
        state = null;
    }

    @Test
    public void executeCommandTest() throws Exception {
        Dice dice = new Dice(1, Color.BLUE);
        DrawableCollection<Dice> modifiedDiceBag = new DrawableCollection<>();
        modifiedDiceBag.addElements(diceBag.getCollection());
        modifiedDiceBag.addElement(dice);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getTemporaryDiceBag()).thenReturn(diceBag);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, state));
        assertEquals(modifiedDiceBag, executor.getTemporaryDiceBag());
    }

    @Test
    public void equalsTest() throws Exception {
        assertEquals(new AddDiceToDiceBag(), command);
        assertNotEquals(new Object(), command);
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new AddDiceToDiceBag().hashCode(), new AddDiceToDiceBag().hashCode());
        assertNotEquals(new AddDiceToDiceBag().hashCode(), new AddDiceToDraftPool().hashCode());
    }

}