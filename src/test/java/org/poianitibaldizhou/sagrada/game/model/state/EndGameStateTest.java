package org.poianitibaldizhou.sagrada.game.model.state;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EndGameStateTest {

    @Mock
    private Game game;
    @Mock
    private Player player1, player2, player3, player4;


    private List<Player> playerList;
    private List<PublicObjectiveCard> publicObjectiveCards;
    private EndGameState endGameState;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        publicObjectiveCards = new ArrayList<>();
        playerList = new ArrayList<>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
        playerList.add(player4);
        when(game.getPlayers()).thenReturn(playerList);
        when(game.getPublicObjectiveCards()).thenReturn(publicObjectiveCards);
        when(game.getIndexOfPlayer(player1)).thenReturn(0);
        when(game.getIndexOfPlayer(player2)).thenReturn(1);
        when(game.getIndexOfPlayer(player3)).thenReturn(2);
        when(game.getIndexOfPlayer(player4)).thenReturn(3);
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
//    public void testPlayer1ScoreHigher(){
//        int player1Score = 10, player2Score = 8, player3Score = 6, player4Score = 4;
//        when(player1.getMultiPlayerScore()).thenReturn(player1Score);
//        when(player2.getMultiPlayerScore()).thenReturn(player2Score);
//        when(player3.getMultiPlayerScore()).thenReturn(player3Score);
//        when(player4.getMultiPlayerScore()).thenReturn(player4Score);
//        endGameState = new EndGameState(game, player1);
//        endGameState.init();
//        endGameState.calculateVictoryPoints();
//        verify(game).setPlayerOutcome(player1, Outcome.WIN);
//        for (Player p: playerList) {
//            if(p != player1)
//                verify(game).setPlayerOutcome(p, Outcome.LOSE);
//        }
//    }
//
//    /**
//     * player1 and player2 have the same score but the player2 score from privateObjective is greater
//     */
//    @Test
//    public void testPlayer1SameScorePlayer2(){
//        int player1Score = 10, player2Score = 10, player3Score = 6, player4Score = 4;
//        when(player1.getMultiPlayerScore()).thenReturn(player1Score);
//        when(player2.getMultiPlayerScore()).thenReturn(player2Score);
//        when(player3.getMultiPlayerScore()).thenReturn(player3Score);
//        when(player4.getMultiPlayerScore()).thenReturn(player4Score);
//        when(player1.getScoreFromPrivateCard()).thenReturn(1);
//        when(player2.getScoreFromPrivateCard()).thenReturn(2);
//        endGameState = new EndGameState(game, player1);
//        endGameState.init();
//        endGameState.calculateVictoryPoints();
//        verify(game).setPlayerOutcome(player2, Outcome.WIN);
//        for (Player p: playerList) {
//            if(p != player2)
//                verify(game).setPlayerOutcome(p, Outcome.LOSE);
//        }
//    }
//
//    /**
//     * player1 and player2 have the same score and same privateObjective score but favor tokens different
//     */
//    @Test
//    public void testPlayer1SameScorePlayer2andSameScoreFromPrivate(){
//        List<String> tokens = new ArrayList<>();
//        MultiPlayerGame game = spy(new MultiPlayerGame("game name", tokens));
//        int player1Score = 10, player2Score = 10, player3Score = 6, player4Score = 4;
//
//        when(player1.getMultiPlayerScore()).thenReturn(player1Score);
//        when(player2.getMultiPlayerScore()).thenReturn(player2Score);
//        when(player3.getMultiPlayerScore()).thenReturn(player3Score);
//        when(player4.getMultiPlayerScore()).thenReturn(player4Score);
//        when(player1.getScoreFromPrivateCard()).thenReturn(2);
//        when(player2.getScoreFromPrivateCard()).thenReturn(2);
//        when(player1.getFavorTokens()).thenReturn(1);
//        when(player2.getFavorTokens()).thenReturn(2);
//        endGameState = new EndGameState(game, player1);
//        endGameState.init();
//        endGameState.calculateVictoryPoints();
//        verify(game).setPlayerOutcome(player2, Outcome.WIN);
//        for (Player p: playerList) {
//            if(p != player2)
//                verify(game).setPlayerOutcome(p, Outcome.LOSE);
//        }
//    }
//
//    /**
//     * player1 and player2 and player 3 have the same score and player1 has same private score of player2
//     * and player1 has same favor tokens of player2 -> player2 wins by reverse order
//     */
//    @Test
//    public void testPlayer1SameScorePlayer2Everything(){
//        int player1Score = 10, player2Score = 10, player3Score = 10, player4Score = 4;
//        when(player1.getMultiPlayerScore()).thenReturn(player1Score);
//        when(player2.getMultiPlayerScore()).thenReturn(player2Score);
//        when(player3.getMultiPlayerScore()).thenReturn(player3Score);
//        when(player4.getMultiPlayerScore()).thenReturn(player4Score);
//        when(player1.getScoreFromPrivateCard()).thenReturn(2);
//        when(player2.getScoreFromPrivateCard()).thenReturn(2);
//        when(player1.getFavorTokens()).thenReturn(2);
//        when(player2.getFavorTokens()).thenReturn(2);
//        endGameState = new EndGameState(game, player1);
//        endGameState.init();
//        endGameState.calculateVictoryPoints();
//        verify(game).setPlayerOutcome(player2, Outcome.WIN);
//        for (Player p: playerList) {
//            if(p != player2)
//                verify(game).setPlayerOutcome(p, Outcome.LOSE);
//        }
//    }
//
//    /**
//     * player1 and player2 and player 3 have the same score and player1 has same private score of player2
//     * and player1 has same favor tokens of player2 -> player2 wins by reverse order
//     */
//    @Test
//    public void testPlayer1SameScorePlayer3Everything(){
//        int player1Score = 10, player2Score = 8, player3Score = 10, player4Score = 4;
//        when(player1.getMultiPlayerScore()).thenReturn(player1Score);
//        when(player2.getMultiPlayerScore()).thenReturn(player2Score);
//        when(player3.getMultiPlayerScore()).thenReturn(player3Score);
//        when(player4.getMultiPlayerScore()).thenReturn(player4Score);
//        when(player1.getScoreFromPrivateCard()).thenReturn(2);
//        when(player3.getScoreFromPrivateCard()).thenReturn(2);
//        when(player1.getFavorTokens()).thenReturn(2);
//        when(player3.getFavorTokens()).thenReturn(2);
//        endGameState = new EndGameState(game, player3);
//        endGameState.init();
//        endGameState.calculateVictoryPoints();
//        verify(game).setPlayerOutcome(player1, Outcome.WIN);
//        for (Player p: playerList) {
//            if(p != player1)
//                verify(game).setPlayerOutcome(p, Outcome.LOSE);
//        }
//    }

    /**
     * player1 and player2 and player 3 have the same score and player1 has same private score of player2
     * and player1 has same favor tokens of player2 -> player2 wins by reverse order
     */
    @Test
    public void calculateVictoryPointsTest(){
        int player1Score = 10, player2Score = 10, player3Score = 10, player4Score = 4;
        when(game.getPlayerScore(player1)).thenReturn(player1Score);
        when(game.getPlayerScore(player2)).thenReturn(player2Score);
        when(game.getPlayerScore(player3)).thenReturn(player3Score);
        when(game.getPlayerScore(player4)).thenReturn(player4Score);
        endGameState = new EndGameState(game, player1);
        endGameState.init();
        endGameState.calculateVictoryPoints();
        ArgumentCaptor<Map<Player, Integer>> argument = ArgumentCaptor.forClass(Map.class);
        verify(game).setPlayersOutcome(argument.capture(), eq(player1));
        assertEquals((Integer) (player1Score), argument.getValue().get(player1));
        assertEquals((Integer) (player2Score), argument.getValue().get(player2));
        assertEquals((Integer) player3Score, argument.getValue().get(player3));
        assertEquals((Integer) player4Score, argument.getValue().get(player4));
    }


    @Test
    public void getCurrentRoundPlayer() throws Exception {
        endGameState = new EndGameState(game, player1);
        assertEquals(player1, endGameState.getCurrentRoundPlayer());
        assertNotEquals(player2, endGameState.getCurrentRoundPlayer());
    }

    @Test
    public void testPlayerOutcomeSinglePlayer(){
        //TODO
    }

}