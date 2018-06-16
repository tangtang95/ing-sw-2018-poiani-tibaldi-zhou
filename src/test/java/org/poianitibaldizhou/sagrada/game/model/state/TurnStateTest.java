package org.poianitibaldizhou.sagrada.game.model.state;

import com.sun.javafx.collections.MappingChange;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ExecutorEvent;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.PlaceDiceState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.SelectActionState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.UseCardState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.EndTurnAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.UseCardAction;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IStateFakeObserver;
import org.poianitibaldizhou.sagrada.network.observers.fakeobserversinterfaces.IToolCardExecutorFakeObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test with dependency class:
 * -
 */
public class TurnStateTest {

    @Mock
    private Game game;

    @Mock
    private Player player1, player2, player3, player4;

    @Mock
    private IStateFakeObserver iStateFakeObserver1, iStateFakeObserver2;

    @Mock
    private ICommand command1;

    @Mock
    private IToolCardExecutorFakeObserver iToolCardExecutorFakeObserver;

    @Mock
    private ToolCard toolCard;

    @Mock
    private ExecutorEvent executorEvent;

    @Mock
    private SelectActionState iPlayerState;

    @Mock
    private PlaceDiceState placeDiceState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<Player> playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);
        when(game.getNextPlayer(player2, Direction.CLOCKWISE)).thenReturn(player3);
        when(game.getNextPlayer(player3, Direction.CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player4, Direction.CLOCKWISE)).thenReturn(player1);
        when(game.getNextPlayer(player4, Direction.COUNTER_CLOCKWISE)).thenReturn(player3);
        when(game.getNextPlayer(player3, Direction.COUNTER_CLOCKWISE)).thenReturn(player2);
        when(game.getNextPlayer(player2, Direction.COUNTER_CLOCKWISE)).thenReturn(player1);
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);

        Node<ICommand> iCommands = new Node<>(command1);

        when(game.getPreCommands(toolCard)).thenReturn(iCommands);

        Map<String, IStateFakeObserver> iStateFakeObserverMap = new HashMap<>();
        iStateFakeObserverMap.put("1", iStateFakeObserver1);
        iStateFakeObserverMap.put("2", iStateFakeObserver2);
        when(game.getStateObservers()).thenReturn(iStateFakeObserverMap);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        player1 = null;
        player2 = null;
        player3 = null;
        player4 = null;
    }

    @Test
    public void initWithoutSkipTurnPlayersTest(){
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        verify(game, times(0)).setState(ArgumentMatchers.any(IStateGame.class));
    }

    @Test
    public void initHasSkipTurnTest(){
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.addSkipTurnPlayer(player1, 1);
        turnState.init();
        verify(game).setState(ArgumentMatchers.any(IStateGame.class));
    }

    @Test
    public void chooseActionByTurnPlayerTest() throws Exception{
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.chooseAction(player1, new PlaceDiceAction());
        assertTrue(turnState.getPlayerState() instanceof PlaceDiceState);
    }

    @Test(expected = InvalidActionException.class)
    public void chooseActionByOtherPlayerTest() throws Exception{
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.chooseAction(player2, new PlaceDiceAction());
    }

    @Test(expected = InvalidActionException.class)
    public void choosePlaceDiceASecondTime() throws Exception {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.addActionUsed(new PlaceDiceAction());
        turnState.chooseAction(player1, new PlaceDiceAction());
    }

    @Test(expected = InvalidActionException.class)
    public void chooseUseCardASecondTime() throws Exception {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.addActionUsed(new UseCardAction());
        turnState.chooseAction(player1, new UseCardAction());
    }

    @Test
    public void nextTurnSinglePlayerTest(){
        List<Player> players = new ArrayList<>();
        players.add(player1);
        when(game.getPlayers()).thenReturn(players);

    }

    @Test
    public void testNextTurnWhenIsFirstTurn() throws Exception {
        TurnState turnState = new TurnState(game, 0,player1, player1, true);
        turnState.init();
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);

        turnState.chooseAction(player1, new EndTurnAction());

        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
        assertEquals("player incorrect", player2, argument.getValue().getCurrentTurnPlayer());
    }

    @Test
    public void testNextTurnWhenIsFirstTurnButBeforeLastPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player3, player1, true);
        turnState.init();
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);

        turnState.chooseAction(player1, new EndTurnAction());
        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
        assertEquals("player incorrect", player2, argument.getValue().getCurrentTurnPlayer());
        assertTrue(argument.getValue().isFirstTurn());
    }

    @Test
    public void testNextTurnWhenIsFirstTurnButLastPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player2, player1, true);
        turnState.init();
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);

        turnState.chooseAction(player1, new EndTurnAction());
        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
        assertEquals("player incorrect", player1, argument.getValue().getCurrentTurnPlayer());
        assertFalse(argument.getValue().isFirstTurn());
    }

    @Test
    public void testNextTurnWhenIsNotFirstTurnAndCurrentPlayer() throws Exception {
        TurnState turnState = new TurnState(game, 0, player1, player1, false);
        turnState.init();
        when(game.getNextPlayer(player1, Direction.COUNTER_CLOCKWISE)).thenReturn(player4);
        when(game.getNextPlayer(player1, Direction.CLOCKWISE)).thenReturn(player2);

        turnState.chooseAction(player1, new EndTurnAction());
        verify(game).setState(ArgumentMatchers.any(RoundEndState.class));
    }

    @Test
    public void getCurrentPlayer() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        assertEquals(player1, turnState.getCurrentRoundPlayer());
        assertNotEquals(player2, turnState.getCurrentRoundPlayer());
    }

    @Test
    public void forceSkipTurn() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.forceSkipTurn();
        verify(game).setState(any());
    }

    @Test(expected = Exception.class)
    public void useCard() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        when(toolCard.getCommands()).thenReturn(new Node<>(command1));
        turnState.useCard(player2,toolCard, iToolCardExecutorFakeObserver);
    }

    @Test
    public void useCard1() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();

        ToolCard toolCard = new ToolCard(Color.PURPLE,
                "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");

        when(iPlayerState.useCard(player1, toolCard)).thenReturn(true);

        turnState.setPlayerState(iPlayerState);
        turnState.useCard(player1,toolCard, iToolCardExecutorFakeObserver);
        //TODO
    }

    @Test(expected = Exception.class)
    public void useCard2() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();

        ToolCard toolCard = new ToolCard(Color.PURPLE,
                "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");

        when(iPlayerState.useCard(player1, toolCard)).thenReturn(false);

        turnState.setPlayerState(iPlayerState);
        turnState.useCard(player1,toolCard, iToolCardExecutorFakeObserver);

    }

    @Test(expected = Exception.class)
    public void placeDiceExceptionTest() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.placeDice(player2, new Dice(2, Color.GREEN), new Position(1,1));
    }

    @Test(expected = Exception.class)
    public void placeDiceException1() throws InvalidActionException, RuleViolationException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        Dice dice = new Dice(2, Color.GREEN);
        Position position = new Position(1,1);
        turnState.setPlayerState(placeDiceState);
        doThrow(RuleViolationException.class).when(placeDiceState).placeDice(player1,dice,position);
        turnState.placeDice(player1, dice,position );
    }

    @Test
    public void placeDice() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.setPlayerState(iPlayerState);
        turnState.placeDice(player1, new Dice(2, Color.GREEN), new Position(1,1));
    }

    @Test(expected = Exception.class)
    public void fireExecutorEvent() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.fireExecutorEvent(executorEvent);
    }

    @Test(expected = Exception.class)
    public void interruptToolCardExecutionExceptionTest() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.interruptToolCardExecution(player2);
    }

    @Test
    public void interruptToolCardExecutionTest() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.interruptToolCardExecution(player1);

    }

    @Test
    public void forceStateChange() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.forceStateChange();
        ArgumentCaptor<TurnState> argument = ArgumentCaptor.forClass(TurnState.class);
        verify(game).setState(argument.capture());
    }

    @Test
    public void releaseToolCardExecution() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.setPlayerState(new UseCardState(turnState));
        turnState.releaseToolCardExecution();
        assertTrue(turnState.getPlayerState() instanceof SelectActionState);
    }

    @Test
    public void hasActionUsed() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.setPlayerState(iPlayerState);
        turnState.placeDice(player1, new Dice(2, Color.GREEN), new Position(1,1));
        assertTrue(turnState.hasActionUsed(new PlaceDiceAction()));
    }

    @Test
    public void getSkipTurnPlayers() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        assertTrue(turnState.getSkipTurnPlayers() != null);
    }

    @Test
    public void setSkipTurnPlayers() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        Map<Player, Integer> skipPlayers = new HashMap<>();
        turnState.setSkipTurnPlayers(skipPlayers);
        assertTrue(turnState.getSkipTurnPlayers() != null);
    }

    @Test
    public void removeToolCard() {
        ToolCard toolCard = new ToolCard(Color.PURPLE,
                "Pinza Sgrossatrice",
                "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1. Non puoi cambiare un 6 in 1 o un 1 in 6",
                "[1-Choose dice][2-Remove dice from DraftPool][4-Modify dice value by 1][8-Place new dice][16-CA]");
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        doNothing().when(game).removeToolCard(toolCard);
        turnState.removeToolCard(toolCard);
        verify(game).removeToolCard(toolCard);

    }

    @Test(expected = Exception.class)
    public void addSkipTurnPlayer() {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        turnState.addSkipTurnPlayer(player1, 500);
    }

    @Test
    public void notifyOnPlaceDiceState() {
    }


    @Test
    public void notifyOnEndTurnState() {
    }
}