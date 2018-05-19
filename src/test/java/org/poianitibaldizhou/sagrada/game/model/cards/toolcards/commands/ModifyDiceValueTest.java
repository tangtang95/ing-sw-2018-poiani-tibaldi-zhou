package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.cli.Command;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

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
        private IStateGame stateGame;
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
            when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
            command = new ModifyDiceValue();
        }

        @Test
        public void executionCommandTest() throws Exception {
            when(executor.getNeededDice()).thenReturn(dice);
            when(executor.getNeededValue()).thenReturn(value);
            assertEquals(expected, command.executeCommand(invokerPlayer, executor, stateGame));
            for (IToolCardExecutorObserver obs : observerList) {
                verify(obs, times(1)).notifyNeedNewValue();
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