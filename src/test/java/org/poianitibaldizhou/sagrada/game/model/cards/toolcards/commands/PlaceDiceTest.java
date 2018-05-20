package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationType;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

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
    private IStateGame stateGame;
    @Mock
    private Player invokerPlayer;
    @Mock
    private DraftPool draftPool;
    @Mock
    private IToolCardExecutorObserver observer1, observer2, observer3;
    @Mock
    private Dice dice;
    @Mock
    private SchemaCard schemaCard;


    private ICommand command;
    private List<IToolCardExecutorObserver> observerList;
    private Position position;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(executor.getObservers()).thenReturn(observerList);
        when(executor.getTemporaryDraftpool()).thenReturn(draftPool);
        when(executor.getTemporarySchemaCard()).thenReturn(schemaCard);
        position = new Position(3, 2);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getPosition()).thenReturn(position);
        }

    @After
    public void tearDown() throws Exception {
        command = null;
        executor = null;
        stateGame = null;
        invokerPlayer = null;
        draftPool = null;
        observerList = null;
        position = null;
        dice = null;
    }

    @Test
    public void executeCommandSucced() throws Exception {
        command = new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
        verify(executor.getTemporarySchemaCard(), times(1)).setDice(dice, position.getRow(), position.getColumn(),
                PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        for (IToolCardExecutorObserver obs : observerList) {
            verify(obs, times(1)).notifyNeedPosition();
        }
    }

    @Test
    public void executeCommandFail() throws Exception {
        command = new PlaceDice(PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL)).thenReturn(true);
        doThrow(RuleViolationException.class).when(schemaCard).setDice(dice, position.getRow(), position.getColumn(),
                PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL);
        assertEquals(CommandFlow.REPEAT, command.executeCommand(invokerPlayer, executor, stateGame));
        for (IToolCardExecutorObserver obs : observerList) {
            verify(obs, times(1)).notifyNeedPosition();
        }
    }

    @Test
    public void executeCommandCantProceed() throws Exception {
        command = new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED)).thenReturn(false);
        assertEquals(CommandFlow.STOP, command.executeCommand(invokerPlayer,executor,stateGame));
    }

    @Test
    public void equals() {
        command = new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        assertEquals(new PlaceDice(PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL), command);
        assertNotEquals(new PlaceDice(PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL), command);
        assertNotEquals(new PlaceDice(PlacementRestrictionType.NONE, DiceRestrictionType.ISOLATED), command);
        assertNotEquals(new Object(), command);
    }

}