package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Outcome;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.PublicObjectiveCard;

import java.util.*;

public class EndGameState extends IStateGame{

    private Map<Player, Integer> scoreMap = new HashMap<>();
    private Player currentPlayer;

    /**
     * Constructor.
     * Create EndGameState to calculate the score of the players and determinate the outcomes of all players
     * @param game the current game
     */
     EndGameState(Game game, Player currentPlayer) {
        super(game);
        this.currentPlayer = currentPlayer;

        calculateScorePlayers(game.getPlayers(), game.getPublicObjectiveCards());
        setOutcomePlayers(game.getPlayers(), game.getIndexOfPlayer(currentPlayer));
        //TODO notify players
    }

    /**
     * Calculate the score of the players and put it into a Map called scoreMap
     *
     * @param players all the players of the game
     * @param publicObjectiveCards all the publicObjective cards to get the score
     */
    private void calculateScorePlayers(List<Player> players, List<PublicObjectiveCard> publicObjectiveCards){
        for (Player player: players) {
            int scorePlayer = 0;
            scorePlayer += player.getScore();
            for (PublicObjectiveCard publicObjectiveCard: publicObjectiveCards) {
                scorePlayer += publicObjectiveCard.getScore(player.getSchemaCard());
            }
            scoreMap.put(player, scorePlayer);
        }
    }

    /**
     * Set the Outcome of each player; first of all it found the winner by VictoryPoints, by PrivateCard points, by
     * FavorTokens and at the end by reverse order of the current player (who has the diceBag at the last round)
     * @param players all players of the game
     * @param indexOfPlayer the current player of the last round
     */
    private void setOutcomePlayers(List<Player> players, int indexOfPlayer){
        List<Player> winners = getWinnersByVictoryPoints(players);
        winners = getWinnersByPrivateCard(winners);
        winners = getWinnersByFavorTokens(winners);
        Player winner = getWinnersByReverseOrder(winners, players, indexOfPlayer);
        winner.setOutcome(Outcome.WIN);
        for (Player other: players) {
            if(!other.equals(winner))
                other.setOutcome(Outcome.LOSE);
        }
    }

    /**
     * Return the list of the winners by VictoryPoints
     *
     * @param players all the players of the game
     * @return the list of winners by VictoryPoints
     */
    private List<Player> getWinnersByVictoryPoints(List<Player> players){
        List<Player> victoryPointWinners = new ArrayList<>();
        int maxVictoryPoints = Integer.MIN_VALUE;
        for (Player player: players) {
            maxVictoryPoints = Math.max(maxVictoryPoints, scoreMap.get(player));
        }
        for (Player player: players) {
            if(scoreMap.get(player) == maxVictoryPoints)
                victoryPointWinners.add(player);
        }
        return victoryPointWinners;
    }

    /**
     * Return the list of winners by PrivateObjectiveCard points based on the list of winners given
     *
     * @param winners the list of player who has won by VictoryPoints
     * @return the list of winners by PrivateObjectiveCard points
     */
    private List<Player> getWinnersByPrivateCard(List<Player> winners){
        List<Player> privateCardWinners = new ArrayList<>();
        int maxPrivateCardPoints = Integer.MIN_VALUE;
        for (Player player: winners) {
            maxPrivateCardPoints = Math.max(maxPrivateCardPoints, player.getScoreFromPrivateCard());
        }
        for (Player player: winners) {
            if(maxPrivateCardPoints == player.getScoreFromPrivateCard())
                privateCardWinners.add(player);
        }
        return privateCardWinners;
    }

    /**
     * Return the list of winners by FavorTokens (who has most tokens)
     *
     * @param winners the list of players who has won by PrivateObjectiveCards
     * @return the list of winners by FavorTokens
     */
    private List<Player> getWinnersByFavorTokens(List<Player> winners){
        List<Player> favorTokensWinners = new ArrayList<>();
        int maxFavorTokens = Integer.MIN_VALUE;
        for (Player player: winners) {
            maxFavorTokens = Math.max(maxFavorTokens, player.getFavorTokens());
        }
        for (Player player: winners) {
            if(maxFavorTokens == player.getFavorTokens())
                favorTokensWinners.add(player);
        }
        return favorTokensWinners;
    }

    /**
     * Return the only winner by reverse order based on the current player who has the diceBag
     *
     * @param winners the list of winners after FavorTokens
     * @param allPlayers the list of all players to find out if the first player is inside the list of winners given
     * @param currentIndexOfPlayer the current index of the player who has the diceBag
     * @return the only winner by reverse order
     */
    private Player getWinnersByReverseOrder(List<Player> winners, List<Player> allPlayers, int currentIndexOfPlayer) {
        int index = (currentIndexOfPlayer - 1 + allPlayers.size()) % allPlayers.size();
        while (index != currentIndexOfPlayer ) {
            if(winners.contains(allPlayers.get(index)))
                return allPlayers.get(index);
            index = (currentIndexOfPlayer - 1 + allPlayers.size()) % allPlayers.size();
        }
        throw new IllegalArgumentException("No players founded");
    }

}
