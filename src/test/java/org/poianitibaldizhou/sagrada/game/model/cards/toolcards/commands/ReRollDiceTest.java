package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

public class ReRollDiceTest {
    @Mock
    private TurnState stateGame;

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private Player invokerPlayer;

    private ICommand command;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        command = new ReRollDice();
    }

    @Test
    public void executeCommandTest() throws Exception {
        Dice dice = new Dice(1, Color.RED);
        when(executor.getNeededDice()).thenReturn(dice);
        CommandFlow commandFlow = command.executeCommand(invokerPlayer, executor, stateGame);
        assertEquals(CommandFlow.MAIN, commandFlow);
        verify(executor, times(1)).getNeededDice();
        ArgumentCaptor<Dice> argumentCaptor = ArgumentCaptor.forClass(Dice.class);
        verify(executor, times(1)).setNeededDice(argumentCaptor.capture());
        assertEquals(dice.getColor(),argumentCaptor.getValue().getColor());
    }

    @Test
    public void equalsTest() {
        assertEquals(command, new ReRollDice());
        assertNotEquals(command, new AddDiceToDiceBag());
    }

    @Test
    public void hashCodeTest() {
        assertEquals(new ReRollDice().hashCode(), new ReRollDice().hashCode());
        assertNotEquals(new ReRollDice().hashCode(), new AddDiceToDiceBag().hashCode());
    }
}
