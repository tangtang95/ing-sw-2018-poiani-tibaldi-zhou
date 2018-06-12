package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Direction;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.Node;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.PlaceDiceState;
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
        turnState.forceSkipTurn();
        verify(game).setState(any());
    }

    @Test(expected = Exception.class)
    public void useCard() throws InvalidActionException {
        TurnState turnState = new TurnState(game, 0, player1, player1, true);
        when(toolCard.getCommands()).thenReturn(new Node<>(command1));
        turnState.useCard(player2,toolCard, iToolCardExecutorFakeObserver);
    }

    @Test
    public void placeDice() {
    }

    @Test
    public void fireExecutorEvent() {
    }

    @Test
    public void interruptToolCardExecution() {
    }

    @Test
    public void forceStateChange() {
    }

    @Test
    public void releaseToolCardExecution() {
    }

    @Test
    public void hasActionUsed() {
    }

    @Test
    public void getSkipTurnPlayers() {
    }

    @Test
    public void setSkipTurnPlayers() {
    }

    @Test
    public void addSkipTurnPlayer() {
    }

    @Test
    public void notifyOnPlaceDiceState() {
    }


    @Test
    public void notifyOnEndTurnState() {
    }
}