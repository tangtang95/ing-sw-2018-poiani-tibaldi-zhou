package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import java.util.ArrayList;
import java.util.List;

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
    }

    @After
    public void tearDown() throws Exception {
        diceBag = null;
        toolCards = null;
        publicObjectiveCards = null;
        setupGameState = null;
    }

    @Test
    public void constructorSinglePlayerTest() throws Exception{
        int difficulty = 3;
        when(game.isSinglePlayer()).thenReturn(true);
        when(game.getNumberOfToolCardForGame()).thenReturn(difficulty);
        when(game.getNumberOfPublicObjectiveCardForGame())
                .thenReturn(SinglePlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
        setupGameState = new SetupGameState(game);
        setupGameState.init();
        verify(game).initDiceBag();
        verify(game, times(SinglePlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS))
                .addPublicObjectiveCard(ArgumentMatchers.any(PublicObjectiveCard.class));
        verify(game, times(difficulty)).addToolCard(ArgumentMatchers.any(ToolCard.class));
    }

    @Test
    public void constructorMultiPlayerTest() throws Exception{
        when(game.isSinglePlayer()).thenReturn(false);
        when(game.getNumberOfPublicObjectiveCardForGame())
                .thenReturn(MultiPlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS);
        when(game.getNumberOfToolCardForGame()).thenReturn(MultiPlayerGame.NUMBER_OF_TOOL_CARDS);
        setupGameState = new SetupGameState(game);
        setupGameState.init();
        verify(game).initDiceBag();
        verify(game, times(MultiPlayerGame.NUMBER_OF_PUBLIC_OBJECTIVE_CARDS))
                .addPublicObjectiveCard(ArgumentMatchers.any(PublicObjectiveCard.class));
        verify(game, times(MultiPlayerGame.NUMBER_OF_TOOL_CARDS)).addToolCard(ArgumentMatchers.any(ToolCard.class));
    }

}