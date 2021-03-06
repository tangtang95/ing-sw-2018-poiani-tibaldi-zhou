package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class ModifyDiceValueTest {

    @RunWith(Parameterized.class)
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
        private IToolCardExecutorFakeObserver observer1, observer2, observer3;
        @Mock
        private Dice dice;

        private ICommand command;
        private List<IToolCardExecutorFakeObserver> observerList;
        private CommandFlow expected;
        private int value;

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {CommandFlow.REPEAT, 7},
                    {CommandFlow.REPEAT, 0},
                    {CommandFlow.MAIN, 6},
                    {CommandFlow.MAIN, 1}
            });
        }

        public ParameterizedPart(CommandFlow expected, int value) {
            this.expected = expected;
            this.value = value;

            MockitoAnnotations.initMocks(this);
            observerList = new ArrayList<>();
            observerList.add(observer1);
            observerList.add(observer2);
            observerList.add(observer3);
            when(executor.getObservers()).thenReturn(observerList);
            when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
            command = new ModifyDiceValue();
        }

        @Test
        public void executionCommandTest() throws Exception {
            when(executor.getNeededDice()).thenReturn(dice);
            when(executor.getNeededValue()).thenReturn(value);
            assertEquals(expected, command.executeCommand(invokerPlayer, executor, stateGame));
            for (IToolCardExecutorFakeObserver obs : observerList) {
                verify(obs, times(1)).notifyNeedNewValue(dice);
            }
        }
    }

    public static class NonParameterizedPart {
        @Test
        public void equals() {
            ICommand command = new ModifyDiceValue();
            assertEquals(new ModifyDiceValue(), command);
            assertNotEquals(command, new ModifyDiceValueByDelta(1));
        }
    }
}