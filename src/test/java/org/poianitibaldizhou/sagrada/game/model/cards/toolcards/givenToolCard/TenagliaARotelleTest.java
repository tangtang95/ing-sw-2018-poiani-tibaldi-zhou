package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.givenToolCard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TenagliaARotelleTest {

    @Mock
    private Game game;
    @Mock
    private TurnState state;
    @Mock
    private Player player1, player2, player3, player4;

    private ToolCard toolCard;

    private TurnState turnState;

    private DraftPool draftPool;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        draftPool = new DraftPool();
        draftPool.addDice(new Dice(4, Color.YELLOW));
        when(game.getPlayers()).thenReturn(players);
        when(game.getDraftPool()).thenReturn(draftPool);
        when(game.getState()).thenReturn(state);
        toolCard = new ToolCard(Color.RED, "Tenaglia a Rotelle",
                "Dopo il tuo primo turno scegli immediatamente un altro dado. Salta il tuo secondo " +
                        "turno in questo round"
                ,"[1-Check first turn][2-Wait turn end][4-Choose dice][8-Place dice][16-Skip second turn][32-CA]");
    }

    @After
    public void tearDown() throws Exception {
        toolCard = null;
        game = null;
        player1 = null;
        player2 = null;
        turnState = null;
    }

    @Test
    public void test(){}
/*
    @Test
    public void mainFlowTest() throws Exception {
        turnState = new TurnState(game, 0, player1, player1, true);
        turnState.init();
        when(game.getState()).thenReturn(turnState);
        when(player1.isCardUsable(toolCard)).thenReturn(true);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                turnState.setPlayerState(new SelectActionState(turnState));
                return null;
            }
        }).when(game).releaseToolCardExecution();

        turnState.chooseAction(player1, new UseCardAction());
        turnState.useCard(player1, toolCard, mock(IToolCardExecutorObserver.class));
        while (!(turnState.getPlayerState() instanceof SelectActionState))
            Thread.sleep(100);
        Dice dice = new Dice(4, Color.YELLOW);
        Position pos = new Position(0, 2);
        turnState.userFireExecutorEvent(new DiceExecutorEvent(dice));
        turnState.userFireExecutorEvent(new PositionExecutorEvent(pos));
        turnState.chooseAction(player1, new EndTurnAction());
        turnState.getToolCardExecutor().join();
        verify(executor).setDice(dice, pos.getRow(), pos.getColumn(),
                PlacementRestrictionType.NUMBER_COLOR, DiceRestrictionType.NORMAL);
    }*/
}
