package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SinglePlayerGameStrategy implements IGameStrategy{

    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 2;
    public static final int NUMBER_OF_DICES_TO_DRAW = 4;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 2;

    private SinglePlayerGame singlePlayerGame;

    public SinglePlayerGameStrategy(SinglePlayerGame game){
        this.singlePlayerGame = game;
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return singlePlayerGame.getDifficulty();
    }

    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    @Override
    public int getPlayerScore(Player player) {
        return player.getSinglePlayerScore();
    }

    @Override
    public int getNumberOfDicesToDraw() {
        return NUMBER_OF_DICES_TO_DRAW;
    }

    @Override
    public int getNumberOfPrivateObjectiveCardForGame() {
        return NUMBER_OF_PRIVATE_OBJECTIVE_CARDS;
    }

    @Override
    public void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        int targetScore = singlePlayerGame.getTargetScore();
        Outcome outcome = (scoreMap.get(currentRoundPlayer) > targetScore) ? Outcome.WIN : Outcome.LOSE;
        singlePlayerGame.setPlayerOutcome(currentRoundPlayer, outcome);
    }

    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    @Override
    public void addNewPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        singlePlayerGame.setPlayer(token, schemaCard, privateObjectiveCards);
    }

    @Override
    public void notifyPlayersEndGame() {
        singlePlayerGame.requestPrivateObjectiveCardFromPlayer();
    }
}
