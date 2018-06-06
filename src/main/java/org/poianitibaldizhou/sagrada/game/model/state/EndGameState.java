package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;

import java.rmi.RemoteException;
import java.util.*;

public class EndGameState extends IStateGame implements ICurrentRoundPlayer {

    private Map<Player, Integer> scoreMap = new HashMap<>();
    private Player currentRoundPlayer;

    /**
     * Constructor.
     * Create EndGameState to calculate the score of the players and determinate the outcomes of all players
     *
     * @param game               the current game
     * @param currentRoundPlayer the current player of the round who has the diceBag
     */
    public EndGameState(Game game, Player currentRoundPlayer) {
        super(game);
        this.currentRoundPlayer = currentRoundPlayer;
    }

    /**
     * Initialize the state by notifying the observer and
     *
     * {@inheritDoc}
     */
    @Override
    public void init() {
        game.getStateObservers().forEach((key, value) -> value.onEndGame(currentRoundPlayer.getUser()));;

        game.handleEndGame();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void choosePrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) {
        game.selectPrivateObjectiveCard(player, privateObjectiveCard);
        calculateVictoryPoints();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateVictoryPoints() {
        calculateScorePlayers(game.getPlayers(), game.getPublicObjectiveCards());
        game.setPlayersOutcome(scoreMap, currentRoundPlayer);
        game.getStateObservers().forEach((key, value) -> value.onVictoryPointsCalculated(scoreMap));
    }

    /**
     * Calculate the score of the players and put it into a Map called scoreMap
     *
     * @param players              all the players of the game
     * @param publicObjectiveCards all the publicObjective cards to get the score
     */
    private void calculateScorePlayers(List<Player> players, List<PublicObjectiveCard> publicObjectiveCards) {
        for (Player player : players) {
            int scorePlayer = 0;
            scorePlayer += game.getPlayerScore(player);
            for (PublicObjectiveCard publicObjectiveCard : publicObjectiveCards) {
                scorePlayer += publicObjectiveCard.getScore(player.getSchemaCard());
            }
            scoreMap.put(player, scorePlayer);
        }
    }

    @Override
    public Player getCurrentRoundPlayer() {
        return currentRoundPlayer;
    }

}
