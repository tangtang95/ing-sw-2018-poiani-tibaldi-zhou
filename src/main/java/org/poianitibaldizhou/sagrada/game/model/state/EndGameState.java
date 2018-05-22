package org.poianitibaldizhou.sagrada.game.model.state;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PublicObjectiveCard;

import java.util.*;

public class EndGameState extends IStateGame implements ICurrentRoundPlayer {

    private Map<Player, Integer> scoreMap = new HashMap<>();
    private Player currentRoundPlayer;

    /**
     * Constructor.
     * Create EndGameState to calculate the score of the players and determinate the outcomes of all players
     *
     * @param game               the current game
     * @param currentRoundPlayer
     */
    EndGameState(Game game, Player currentRoundPlayer) {
        super(game);
        this.currentRoundPlayer = currentRoundPlayer;
    }

    /**
     * copy-constructor
     *
     * @param gameState the gameState to copy
     */
    private EndGameState(EndGameState gameState) {
        super(gameState.game);
        this.currentRoundPlayer = gameState.currentRoundPlayer;
        for (Player player : gameState.scoreMap.keySet())
            this.scoreMap.put(player, gameState.scoreMap.get(player));
    }

    @Override
    public void init() {
        game.getStateObservers().forEach(obs -> obs.onEndGame(currentRoundPlayer.getUser()));
        game.notifyPlayersEndGame();
    }

    @Override
    public void choosePrivateObjectiveCard(Player player, PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        game.selectPrivateObjectiveCard(player, privateObjectiveCard);
        calculateVictoryPoints();
    }

    @Override
    public void calculateVictoryPoints() {
        calculateScorePlayers(game.getPlayers(), game.getPublicObjectiveCards());
        game.setPlayersOutcome(scoreMap, currentRoundPlayer);
        //TODO notify
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

    public static IStateGame newInstance(IStateGame egs) {
        if (egs == null)
            return null;
        return new EndGameState((EndGameState) egs);
    }
}
