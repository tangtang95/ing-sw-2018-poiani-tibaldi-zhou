package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Dependency class with:
 * - GameInjector
 * - RoundStartState constructor
 */
public class SetupGameStateTest {

    private SetupGameState setupGameState;

    @Mock
    private static Player player1, player2, player3, player4;

    private List<Player> playerList;

    private DrawableCollection<Dice> diceBag;
    private List<ToolCard> toolCards;
    private List<PublicObjectiveCard> publicObjectiveCards;

    @Mock
    private Game game;
    @Mock
    private IGameStrategy gameStrategy;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        diceBag = new DrawableCollection<>();
        toolCards = new ArrayList<>();
        publicObjectiveCards = new ArrayList<>();
        when(game.getDiceBag()).thenReturn(diceBag);
        when(game.getToolCards()).thenReturn(toolCards);
        when(game.getPublicObjectiveCards()).thenReturn(publicObjectiveCards);
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getGameStrategy()).thenReturn(gameStrategy);
    }

    @After
    public void tearDown() throws Exception {
        diceBag = null;
        toolCards = null;
        publicObjectiveCards = null;
        setupGameState = null;
    }

    @Test
    public void constructorSinglePlayerTest(){
        int difficulty = 3;
        when(game.isSinglePlayer()).thenReturn(true);
        when(gameStrategy.getNumberOfToolCardForGame()).thenReturn(difficulty);
        setupGameState = new SetupGameState(game);
        assertEquals("DiceBag size error", 90,  diceBag.size());
        assertEquals("PublicObjectiveCards size error",
                SinglePlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS, publicObjectiveCards.size());
        assertEquals("ToolCards size error", difficulty, toolCards.size());
    }

    @Test
    public void constructorMultiPlayerTest(){
        when(game.isSinglePlayer()).thenReturn(false);
        setupGameState = new SetupGameState(game);
        assertEquals("DiceBag size error", 90,  diceBag.size());
        assertEquals("PublicObjectiveCards size error",
                MultiPlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS, publicObjectiveCards.size());
        assertEquals("ToolCards size error",
                MultiPlayerGameStrategy.NUMBER_OF_TOOL_CARDS, toolCards.size());
    }

    @Test
    public void readyGameTest() throws Exception {
        when(game.isSinglePlayer()).thenReturn(false);
        setupGameState = new SetupGameState(game);
        /*doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                RoundStartState roundStartState = invocationOnMock.getArgument(0);
                assertTrue(playerList.contains(roundStartState.getCurrentRoundPlayer()));
                return null;
            }
        }).when(game).setState(ArgumentMatchers.any(RoundStartState.class));*/
        setupGameState.readyGame();
        ArgumentCaptor<RoundStartState> argument = ArgumentCaptor.forClass(RoundStartState.class);
        verify(game).setState(argument.capture());
        assertTrue(playerList.contains(argument.getValue().getCurrentRoundPlayer()));
    }

}