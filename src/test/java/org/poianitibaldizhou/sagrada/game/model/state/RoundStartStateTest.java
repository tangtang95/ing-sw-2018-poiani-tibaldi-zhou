package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.board.DraftPool;
import org.poianitibaldizhou.sagrada.game.model.board.DrawableCollection;
import org.poianitibaldizhou.sagrada.game.model.players.Player;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RoundStartStateTest {

    @Mock
    private Game game;
    @Mock
    private DraftPool draftPool;
    @Mock
    private Player currentPlayer, otherPlayer;

    private RoundStartState roundStartState;
    private DrawableCollection<Dice> diceBag;

    /**
     * Dependency with GameInjector
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        diceBag = new DrawableCollection<>();
        for (int i = 0; i < 90; i++) {
            diceBag.addElement(mock(Dice.class));
        }
        when(game.getDiceBag()).thenReturn(diceBag);
        when(game.getDraftPool()).thenReturn(draftPool);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
        diceBag = null;
    }

    @Test
    public void throwDicesTestSinglePlayer() throws Exception {
        assertEquals("DiceBag size error", 90, diceBag.size());

        when(game.isSinglePlayer()).thenReturn(true);
        when(game.getNumberOfDicesToDraw()).thenReturn(SinglePlayerGame.NUMBER_OF_DICES_TO_DRAW);
        roundStartState = new RoundStartState(game, 0, currentPlayer);
        roundStartState.init();
        roundStartState.throwDices(currentPlayer);
        verify(game).addDicesToDraftPoolFromDiceBag();
        verify(game).setState(ArgumentMatchers.any(TurnState.class));
    }

    @Test(expected = InvalidActionException.class)
    public void throwDicesTestException() throws Exception{
        when(game.isSinglePlayer()).thenReturn(true);
        when(game.getNumberOfDicesToDraw()).thenReturn(SinglePlayerGame.NUMBER_OF_DICES_TO_DRAW);
        roundStartState = new RoundStartState(game, 0, currentPlayer);
        roundStartState.init();
        roundStartState.throwDices(otherPlayer);
    }

    @Test
    public void throwDicesTestMultiPlayer() throws Exception{
        assertEquals("DiceBag size error", 90, diceBag.size());
        int numberOfPlayers = 4;

        when(game.isSinglePlayer()).thenReturn(false);
        when(game.getNumberOfPlayers()).thenReturn(numberOfPlayers);
        when(game.getNumberOfDicesToDraw()).thenReturn(numberOfPlayers*2 + 1);
        roundStartState = new RoundStartState(game, 0, currentPlayer);
        roundStartState.init();
        roundStartState.throwDices(currentPlayer);
        verify(game).addDicesToDraftPoolFromDiceBag();
        verify(game).setState(ArgumentMatchers.any(TurnState.class));
    }

    @Test
    public void getCurrentPlayer() throws Exception {
        roundStartState = new RoundStartState(game, 0, currentPlayer);
        roundStartState.init();
        assertEquals(currentPlayer, roundStartState.getCurrentRoundPlayer());
        assertNotEquals(otherPlayer, roundStartState.getCurrentRoundPlayer());
    }

}