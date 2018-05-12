package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.EmptyCollectionException;
import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SinglePlayerGameStrategy implements IGameStrategy{

    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 2;
    public static final int NUMBER_OF_DICES_TO_DRAW = 4;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 2;

    private int gameDifficulty;
    private List<PrivateObjectiveCard> privateObjectiveCards;
    private PrivateObjectiveCard privateObjectiveCardSelected;

    public SinglePlayerGameStrategy(int difficulty){
        this.gameDifficulty = difficulty;
        privateObjectiveCards = new ArrayList<>();
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return gameDifficulty;
    }

    @Override
    public int getNumberOfPublicObjectiveCardForGame() {
        return NUMBER_OF_PUBLIC_OBJECTIVE_CARDS;
    }

    @Override
    public int getPlayerScore(Player player) {
        return player.getSinglePlayerScore(privateObjectiveCardSelected);
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
    public void selectPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException {
        if(!privateObjectiveCards.contains(privateObjectiveCard))
            throw new InvalidActionException();
        privateObjectiveCardSelected = privateObjectiveCard;
    }

    @Override
    public void setPrivateObjectiveCard(Player player, DrawableCollection<PrivateObjectiveCard> privateObjectiveCards) {
        try {
            this.privateObjectiveCards.add(privateObjectiveCards.draw());
        } catch (EmptyCollectionException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.toString(), e);
        }
    }

    @Override
    public void setPlayersOutcome(Game game, Map<Player, Integer> scoreMap, Player currentRoundPlayer) {
        int targetScore = game.getTargetScore();
        Outcome outcome = (scoreMap.get(currentRoundPlayer) > targetScore) ? Outcome.WIN : Outcome.LOSE;
        game.setPlayerOutcome(currentRoundPlayer, outcome);
    }


}
