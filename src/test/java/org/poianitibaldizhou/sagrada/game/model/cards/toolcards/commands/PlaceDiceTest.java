package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PlaceDiceTest {
    @Mock
    private ToolCardExecutor executor;
    @Mock
    private Game game;
    @Mock
    private Player invokerPlayer;
    @Mock
    private DraftPool draftPool;
    @Mock
    private IToolCardExecutorObserver observer1, observer2, observer3;

    private ICommand command;
    private List<IToolCardExecutorObserver> observerList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(executor.getObservers()).thenReturn(observerList);
        when(game.getDraftPool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        game = null;
        invokerPlayer = null;
        draftPool = null;
        observerList = null;
    }
    @Test
    public void executeCommand() throws Exception {
        command = new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        Dice dice = mock(Dice.class);
        Position position = mock(Position.class);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getPosition()).thenReturn(position);
        command.executeCommand(invokerPlayer, executor, game);
        verify(game).setDiceOnSchemaCardPlayer(invokerPlayer, dice, position.getRow(), position.getColumn(),
                PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);

        doThrow(new ExecutionCommandException()).when(game)
                .setDiceOnSchemaCardPlayer(invokerPlayer, dice, position.getRow(), position.getColumn(),
                PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        try {
            command.executeCommand(invokerPlayer, executor, game);
            fail("exception expected");
        }catch (ExecutionCommandException e){
            assertNotEquals(null, e);
        }

    }

    @Test
    public void equals() throws Exception {
    }

}