package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        when(gameStrategy.getNumberOfPublicObjectiveCardForGame())
                .thenReturn(SinglePlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
        setupGameState = new SetupGameState(game);
        setupGameState.init();
        verify(game).initDiceBag();
        verify(game, times(SinglePlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS))
                .addPublicObjectiveCard(ArgumentMatchers.any(PublicObjectiveCard.class));
        verify(game, times(difficulty)).addToolCard(ArgumentMatchers.any(ToolCard.class));
    }

    @Test
    public void constructorMultiPlayerTest(){
        when(game.isSinglePlayer()).thenReturn(false);
        when(gameStrategy.getNumberOfPublicObjectiveCardForGame())
                .thenReturn(MultiPlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
        when(gameStrategy.getNumberOfToolCardForGame()).thenReturn(MultiPlayerGameStrategy.NUMBER_OF_TOOL_CARDS);
        setupGameState = new SetupGameState(game);
        setupGameState.init();
        verify(game).initDiceBag();
        verify(game, times(MultiPlayerGameStrategy.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS))
                .addPublicObjectiveCard(ArgumentMatchers.any(PublicObjectiveCard.class));
        verify(game, times(MultiPlayerGameStrategy.NUMBER_OF_TOOL_CARDS)).addToolCard(ArgumentMatchers.any(ToolCard.class));
    }

    @Test
    public void readyGameTest() throws Exception {
        when(game.isSinglePlayer()).thenReturn(false);
        setupGameState = new SetupGameState(game);
        setupGameState.readyGame();
        ArgumentCaptor<RoundStartState> argument = ArgumentCaptor.forClass(RoundStartState.class);
        verify(game).setState(argument.capture());
        assertTrue(playerList.contains(argument.getValue().getCurrentRoundPlayer()));
    }

}