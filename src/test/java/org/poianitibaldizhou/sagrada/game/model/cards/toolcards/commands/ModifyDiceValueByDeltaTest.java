package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class ModifyDiceValueByDeltaTest {

    @RunWith(value = Parameterized.class)
    public static class ParameterizedPart {

        @Mock
        private ToolCardExecutor executor;
        @Mock
        private TurnState stateGame;
        @Mock
        private Player invokerPlayer;
        @Mock
        private DraftPool draftPool;
        @Mock
        private IToolCardExecutorObserver observer1, observer2, observer3;
        @Mock
        private Dice dice;

        private ICommand command;
        private List<IToolCardExecutorObserver> observerList;
        private CommandFlow expected;
        private int delta;
        private int diceNumber;
        private int newValue;

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {CommandFlow.REPEAT, 1, 3, 5},
                    {CommandFlow.REPEAT, 1, 3, 1},
                    {CommandFlow.MAIN, 1, 3, 2},
                    {CommandFlow.MAIN, 1, 3, 4},
                    {CommandFlow.REPEAT,2,1,1},
                    {CommandFlow.REPEAT,2,1,2},
                    {CommandFlow.REPEAT,2,1,4},
                    {CommandFlow.REPEAT,2,1,5},
                    {CommandFlow.REPEAT,2,1,6},
                    {CommandFlow.MAIN,2,1, 3},
                    {CommandFlow.REPEAT, 2,1,7},
                    {CommandFlow.REPEAT,2,1,8}
            });
        }

        public ParameterizedPart(CommandFlow expected, int delta, int diceNumber, int newValue) {
            this.expected = expected;
            this.delta = delta;
            this.diceNumber = diceNumber;
            this.newValue = newValue;

            MockitoAnnotations.initMocks(this);
            observerList = new ArrayList<>();
            observerList.add(observer1);
            observerList.add(observer2);
            observerList.add(observer3);
            when(executor.getObservers()).thenReturn(observerList);
            when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        }

        @Test
        public void executeCommand() throws Exception {
            command = new ModifyDiceValueByDelta(delta);
            when(dice.getNumber()).thenReturn(diceNumber);
            when(executor.getNeededDice()).thenReturn(dice);
            when(executor.getNeededValue()).thenReturn(newValue);
            assertEquals(expected, command.executeCommand(invokerPlayer, executor, stateGame));

            for (IToolCardExecutorObserver obs : observerList) {
                verify(obs, times(1)).notifyNeedNewDeltaForDice(dice.getNumber(), delta);
            }
        }
    }

    public static class NonParameterizedPart {

        @Test(expected = IllegalArgumentException.class)
        public void testConstructorTooHighNumber() throws Exception {
            ICommand command = new ModifyDiceValueByDelta(6);
        }

        @Test(expected = IllegalArgumentException.class)
        public void testConstructorTooLowNumber() throws Exception {
            ICommand command = new ModifyDiceValueByDelta(0);
        }


        @Test
        public void equals() throws Exception {
            ICommand command = new ModifyDiceValueByDelta(1);
            assertEquals(new ModifyDiceValueByDelta(1), command);
            assertNotEquals(new ModifyDiceValueByDelta(2), command);
            assertNotEquals(command, null);
        }
    }
}