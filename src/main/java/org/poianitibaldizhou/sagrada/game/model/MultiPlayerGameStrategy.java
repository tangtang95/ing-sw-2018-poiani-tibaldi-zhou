package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiPlayerGameStrategy implements IGameStrategy {

    public static final int NUMBER_OF_TOOL_CARDS = 3;
    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 3;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 1;

    private MultiPlayerGame multiPlayerGame;

    public MultiPlayerGameStrategy(MultiPlayerGame game){
        this.multiPlayerGame = game;
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return NUMBER_OF_TOOL_CARDS;
    }

    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    @Override
    public int getPlayerScore(Player player) {
        return player.getMultiPlayerScore();
    }

    @Override
    public int getNumberOfDicesToDraw() {
        return multiPlayerGame.getNumberOfPlayers()*2 + 1;
    }

    @Override
    public int getNumberOfPrivateObjectiveCardForGame() {
        return NUMBER_OF_PRIVATE_OBJECTIVE_CARDS;
    }


    /**
     * Set the Outcome of each player; first of all it found the winner by VictoryPoints, by PrivateCard points, by
     * FavorTokens and at the end by reverse order of the current player (who has the diceBag at the last round)
     *
     * @param scoreMap the score of each player
     * @param currentRoundPlayer the current player of the last round
     */
    @Override
    public void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        List<Player> players = multiPlayerGame.getPlayers();
        List<Player> winners = getWinnersByVictoryPoints(players, scoreMap);
        winners = getWinnersByPrivateCard(winners);
        winners = getWinnersByFavorTokens(winners);
        Player winner = getWinnersByReverseOrder(winners, players, multiPlayerGame.getIndexOfPlayer(currentRoundPlayer));
        multiPlayerGame.setPlayerOutcome(winner, Outcome.WIN);
        for (Player other : players) {
            if (!other.equals(winner))
                multiPlayerGame.setPlayerOutcome(other, Outcome.LOSE);
        }
    }

    @Override
    public boolean isSinglePlayer() {
        return false;
    }

    @Override
    public void addNewPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        multiPlayerGame.addNewPlayer(token, schemaCard, privateObjectiveCards);
    }

    @Override
    public void notifyPlayersEndGame() {
        multiPlayerGame.calculateOutcome();
    }

    /**
     * Return the list of the winners by VictoryPoints
     *
     * @param players all the players of the game
     * @param scoreMap the score of each player
     * @return the list of winners by VictoryPoints
     */
    private List<Player> getWinnersByVictoryPoints(List<Player> players, Map<Player, Integer> scoreMap) {
        List<Player> victoryPointWinners = new ArrayList<>();
        int maxVictoryPoints = Integer.MIN_VALUE;
        for (Player player : players) {
            maxVictoryPoints = Math.max(maxVictoryPoints, scoreMap.get(player));
        }
        for (Player player : players) {
            if (scoreMap.get(player) == maxVictoryPoints)
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
    private List<Player> getWinnersByPrivateCard(List<Player> winners) {
        if (winners.size() == 1)
            return winners;
        List<Player> privateCardWinners = new ArrayList<>();
        int maxPrivateCardPoints = Integer.MIN_VALUE;
        for (Player player : winners) {
            maxPrivateCardPoints = Math.max(maxPrivateCardPoints, player.getScoreFromPrivateCard());
        }
        for (Player player : winners) {
            if (maxPrivateCardPoints == player.getScoreFromPrivateCard())
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
    private List<Player> getWinnersByFavorTokens(List<Player> winners) {
        if (winners.size() == 1)
            return winners;
        List<Player> favorTokensWinners = new ArrayList<>();
        int maxFavorTokens = Integer.MIN_VALUE;
        for (Player player : winners) {
            maxFavorTokens = Math.max(maxFavorTokens, player.getFavorTokens());
        }
        for (Player player : winners) {
            if (maxFavorTokens == player.getFavorTokens())
                favorTokensWinners.add(player);
        }
        return favorTokensWinners;
    }

    /**
     * Return the only winner by reverse order based on the current player who has the diceBag
     *
     * @param winners              the list of winners after FavorTokens
     * @param allPlayers           the list of all players to find out if the first player is inside the list of winners given
     * @param currentIndexOfPlayer the current index of the player who has the diceBag
     * @return the only winner by reverse order
     */
    private Player getWinnersByReverseOrder(List<Player> winners, List<Player> allPlayers, int currentIndexOfPlayer) {
        if (winners.size() == 1)
            return winners.get(0);
        int index = (currentIndexOfPlayer - 1 + allPlayers.size()) % allPlayers.size();
        while (index != currentIndexOfPlayer) {
            if (winners.contains(allPlayers.get(index)))
                return allPlayers.get(index);
            index = (index - 1 + allPlayers.size()) % allPlayers.size();
        }
        if (winners.contains(allPlayers.get(index)))
            return allPlayers.get(index);
        throw new IllegalArgumentException("No players founded");
    }
}
