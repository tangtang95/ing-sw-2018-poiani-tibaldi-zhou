package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

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
    private TurnState stateGame;
    @Mock
    private Player invokerPlayer;
    @Mock
    private DraftPool draftPool;
    @Mock
    private IToolCardExecutorFakeObserver observer1, observer2, observer3;
    @Mock
    private Dice dice;
    @Mock
    private SchemaCard schemaCard;


    private ICommand command;
    private List<IToolCardExecutorFakeObserver> observerList;
    private Position position;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(executor.getObservers()).thenReturn(observerList);
        when(executor.getTemporaryDraftPool()).thenReturn(draftPool);
        when(executor.getTemporarySchemaCard()).thenReturn(schemaCard);
        position = new Position(3, 2);
        when(executor.getNeededDice()).thenReturn(dice);
        when(executor.getNeededPosition()).thenReturn(position);
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
    public void executeCommandFailNewPlacement() throws Exception{
        command = new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER_COLOR, true);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)).thenReturn(true);
        when(stateGame.hasActionUsed(new PlaceDiceAction())).thenReturn(true);
        assertEquals(CommandFlow.PLACEMENT_ALREADY_DONE, command.executeCommand(invokerPlayer, executor, stateGame));
    }

    @Test
    public void executeCommandSucceed() throws Exception {
        command = new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER_COLOR, false);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL)).thenReturn(true);
        assertEquals(CommandFlow.MAIN, command.executeCommand(invokerPlayer, executor, stateGame));
        verify(executor.getTemporarySchemaCard(), times(1)).setDice(dice, position,
                PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
        for (IToolCardExecutorFakeObserver obs : observerList) {
            verify(obs, times(1)).notifyNeedPositionForRemoving(schemaCard);
        }
    }

    @Test
    public void executeCommandFail() throws Exception {
        command = new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER, false);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL)).thenReturn(true);
        doThrow(RuleViolationException.class).when(schemaCard).setDice(dice, position,
                PlacementRestrictionType.NUMBER, DiceRestrictionType.NORMAL);
        assertEquals(CommandFlow.REPEAT, command.executeCommand(invokerPlayer, executor, stateGame));
        for (IToolCardExecutorFakeObserver obs : observerList) {
            verify(obs, times(1)).notifyNeedPositionForRemoving(schemaCard);
        }
    }

    @Test
    public void executeCommandCantProceed() throws Exception {
        command = new PlaceDice(DiceRestrictionType.ISOLATED, PlacementRestrictionType.NUMBER_COLOR, false);
        when(schemaCard.isDicePositionable(dice, PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.ISOLATED)).thenReturn(false);
        assertEquals(CommandFlow.DICE_CANNOT_BE_PLACED_ANYWHERE, command.executeCommand(invokerPlayer,executor,stateGame));
    }

    @Test
    public void equals() {
        command = new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER_COLOR, false);
        assertEquals(new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER_COLOR, false), command);
        assertNotEquals(new PlaceDice(DiceRestrictionType.NORMAL, PlacementRestrictionType.NUMBER, false), command);
        assertNotEquals(new PlaceDice(DiceRestrictionType.ISOLATED, PlacementRestrictionType.NONE, false), command);
        assertNotEquals(new Object(), command);
    }

}