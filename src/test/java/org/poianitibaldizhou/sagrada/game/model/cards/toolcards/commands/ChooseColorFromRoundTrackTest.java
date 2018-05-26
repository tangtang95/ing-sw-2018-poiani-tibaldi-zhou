package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.RoundTrack;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class ChooseColorFromRoundTrackTest {

    @RunWith(Parameterized.class)
    public static class ParameterizedPart {
        @Mock
        private ToolCardExecutor executor;
        @Mock
        private TurnState state;
        @Mock
        private Player invokerPlayer;
        @Mock
        private IToolCardExecutorObserver observer1, observer2, observer3;

        private ICommand command;
        private List<IToolCardExecutorObserver> observerList;
        private RoundTrack roundTrack;
        private Set<Color> colors;

        @Parameterized.Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    {new Object[]{Color.BLUE, Color.RED}, new Object[][]{
                            {new Dice(1, Color.RED), new Dice(2, Color.RED), new Dice(5, Color.BLUE)},
                            {new Dice(4, Color.BLUE), new Dice(5, Color.BLUE), new Dice(5, Color.BLUE)},
                            {new Dice(1, Color.RED), new Dice(2, Color.BLUE), new Dice(5, Color.BLUE)},
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    }},
                    {new Object[]{Color.BLUE, Color.RED, Color.GREEN, Color.PURPLE, Color.YELLOW}, new Object[][]{
                            {new Dice(1, Color.RED), new Dice(2, Color.YELLOW), new Dice(5, Color.BLUE)},
                            {new Dice(4, Color.BLUE), new Dice(5, Color.BLUE), new Dice(5, Color.BLUE)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)},
                            {new Dice(1, Color.RED), new Dice(2, Color.PURPLE), new Dice(5, Color.GREEN)}
                    }}
            });
        }

        public ParameterizedPart(Object[] colorsParam, Object[][] roundTrackParam) throws Exception {
            colors = new HashSet<>();
            roundTrack = new RoundTrack();

            for(Object o : colorsParam)
                colors.add((Color) o);
            for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
                if(roundTrackParam[i] != null)
                    for(Object o : roundTrackParam[i])
                        roundTrack.addDiceToRound((Dice)o , i);
            }

            MockitoAnnotations.initMocks(this);
            observerList = new ArrayList<>();
            observerList.add(observer1);
            observerList.add(observer2);
            observerList.add(observer3);
            when(executor.getObservers()).thenReturn(observerList);
            when(executor.getTemporaryRoundTrack()).thenReturn(roundTrack);
            command = new ChooseColorFromRoundTrack();
        }

        @Test
        public void executeCommand() throws Exception {
            assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, state));
            for (IToolCardExecutorObserver obs: observerList) {
                verify(obs).notifyNeedColor(colors);
            }
        }
    }

    public static class NonParameterizedPart {

        @Test
        public void equals() throws Exception {
            ICommand command = new ChooseColorFromRoundTrack();
            assertEquals(new ChooseColorFromRoundTrack(), command);
            assertNotEquals(new Object(), command);
        }
    }
}