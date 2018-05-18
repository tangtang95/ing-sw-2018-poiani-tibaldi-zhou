package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.ExecutionCommandException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class RemoveDiceTest {

    @Mock
    private ToolCardExecutor executor;

    @Mock
    private Game game;

    @Mock
    private Position position;

    @Mock
    private Player invokerPlayer;

    @Mock
    private IToolCardExecutorObserver observer1, observer2, observer3;

    @DataPoint
    private List<IToolCardExecutorObserver> observerList;

    @DataPoint
    private ICommand removeDiceWithColor;

    @DataPoint
    private ICommand removeDice;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        removeDice = new RemoveDice(PlacementRestrictionType.NONE);
        removeDiceWithColor = new RemoveDice(PlacementRestrictionType.COLOR);
        observerList = new ArrayList<>();
        observerList.add(observer1);
        observerList.add(observer2);
        observerList.add(observer3);
        when(executor.getObservers()).thenReturn(observerList);
        when(position.getColumn()).thenReturn(1);
        when(position.getRow()).thenReturn(1);
    }

    @After
    public void tearDown() {
        game = null;
        invokerPlayer = null;
        removeDiceWithColor = null;
        removeDice = null;
        executor = null;
    }

    @Test
    public void testExecutionFailColorConstraint() throws RemoteException, InterruptedException {
        when(executor.getPosition()).thenReturn(position);
        when(executor.getNeededColor()).thenReturn(Color.BLUE);
        SchemaCard schemaCard = mock(SchemaCard.class);
        when(invokerPlayer.getSchemaCard()).thenReturn(schemaCard);
        when(invokerPlayer.getSchemaCard().getDice(position.getRow(), position.getColumn())).thenReturn(new Dice(1, Color.RED));
        try {
            removeDiceWithColor.executeCommand(invokerPlayer, executor, game);
            fail("Exception expected");
        } catch (ExecutionCommandException e) {
            for(IToolCardExecutorObserver obs: observerList) {
                verify(obs, times(1)).notifyNeedDicePositionOfCertainColor(Color.BLUE);
            }
            verify(executor, times(1)).setNeededPosition(null);
            verify(executor, times(1)).getNeededColor();
            verify(executor, times(1)).getPosition();
        }
    }

    @Test
    public void testExecutionFailNoneConstraint() throws RemoteException, InterruptedException {
        when(executor.getPosition()).thenReturn(position);
        when(game.removeDiceFromSchemaCardPlayer(invokerPlayer,position.getRow(),position.getColumn())).thenReturn(null);

        try {
            removeDice.executeCommand(invokerPlayer, executor, game);
            fail("Exception expected");
        } catch (ExecutionCommandException e) {
            for(IToolCardExecutorObserver obs : observerList) {
                verify(obs, times(1)).notifyNeedPosition();
            }
            verify(executor, times(1)).getPosition();
            verify(executor, times(1)).setNeededPosition(null);
        }
    }

    @Test
    public void testExecutionSucceedColorConstraint() throws InterruptedException, RemoteException, ExecutionCommandException {
        Dice dice = new Dice(1, Color.BLUE);
        when(executor.getPosition()).thenReturn(position);
        when(executor.getNeededDice()).thenReturn(dice);
        SchemaCard schemaCard = mock(SchemaCard.class);
        when(invokerPlayer.getSchemaCard()).thenReturn(schemaCard);
        when(invokerPlayer.getSchemaCard().getDice(position.getRow(), position.getColumn())).thenReturn(dice);
        when(game.removeDiceFromSchemaCardPlayer(invokerPlayer, position.getRow(),position.getColumn())).thenReturn(dice);
        when(executor.getNeededColor()).thenReturn(Color.BLUE);


        CommandFlow commandFlow = removeDiceWithColor.executeCommand(invokerPlayer, executor, game);
        assertEquals("Command execution failed", CommandFlow.MAIN, commandFlow);
        for(IToolCardExecutorObserver obs: observerList) {
            verify(obs, times(1)).notifyNeedDicePositionOfCertainColor(Color.BLUE);
        }
        verify(executor, times(1)).getNeededColor();
        verify(executor, times(1)).getPosition();
        verify(executor, times(1)).setNeededDice(dice);
    }

    @Test
    public void testExecutionSucceedNoneConstraint() throws InterruptedException, RemoteException, ExecutionCommandException {
        Dice dice = new Dice(1, Color.RED);
        when(executor.getPosition()).thenReturn(position);
        when(executor.getNeededDice()).thenReturn(dice);
        SchemaCard schemaCard = mock(SchemaCard.class);
        when(invokerPlayer.getSchemaCard()).thenReturn(schemaCard);
        when(invokerPlayer.getSchemaCard().getDice(position.getRow(), position.getColumn())).thenReturn(dice);
        when(game.removeDiceFromSchemaCardPlayer(invokerPlayer,position.getRow(),position.getColumn())).thenReturn(dice);

        CommandFlow commandFlow = removeDice.executeCommand(invokerPlayer, executor, game);
        assertEquals("Command execution failed", CommandFlow.MAIN, commandFlow);
        for(IToolCardExecutorObserver obs: observerList) {
            verify(obs, times(1)).notifyNeedPosition();
        }
        verify(executor, times(1)).getPosition();
        verify(executor, times(1)).setNeededDice(dice);
    }

    @Test
    public void equalsTest() {
        assertNotEquals(removeDice, removeDiceWithColor);
        assertNotEquals(removeDice, new AddDiceToDiceBag());
        assertNotEquals(removeDiceWithColor, new ChooseDice());
        assertEquals(removeDiceWithColor, new RemoveDice(PlacementRestrictionType.COLOR));
        assertEquals(removeDice, new RemoveDice(PlacementRestrictionType.NONE));
    }

    @Test
    public void constructorTest() {
        for(PlacementRestrictionType type : PlacementRestrictionType.values()) {
            if(type != PlacementRestrictionType.COLOR && type != PlacementRestrictionType.NONE) {
                RemoveDice temp = null;
                try {
                    temp = new RemoveDice(type);
                    fail("Exception expected");
                } catch (Exception e){
                    assertEquals(temp, null);
                }
            }
        }
    }
}
