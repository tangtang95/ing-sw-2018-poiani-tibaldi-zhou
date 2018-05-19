package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;
import java.util.Map;

public class SinglePlayerGame extends Game implements IGameStrategy{

    public static final int NUMBER_OF_PUBLIC_OBJECTIVE_CARDS = 2;
    public static final int NUMBER_OF_DICES_TO_DRAW = 4;
    public static final int NUMBER_OF_PRIVATE_OBJECTIVE_CARDS = 2;

    private final int difficulty;

    public SinglePlayerGame(String singlePlayerToken, int difficulty) {
        super("Single Player Game", singlePlayerToken);
        this.difficulty = difficulty;
    }

    public int getDifficulty(){
        return difficulty;
    }

    /**
     * Return the target score for a single player game. The player score has to be higher than this to win
     *
     * @return the target score
     */
    @Contract(pure = true)
    public int getTargetScore() {
        int targetScore = 0;
        for (int i = 0; i < RoundTrack.NUMBER_OF_TRACK; i++) {
            List<Dice> dices = getRoundTrack().getDices(i);
            for (Dice dice: dices) {
                targetScore += dice.getNumber();
            }
        }
        return targetScore;
    }

    public void setPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        players.add(new Player(token, new ExpendableDice(draftPool), schemaCard, privateObjectiveCards));
    }

    public void requestPrivateObjectiveCardFromPlayer() {
        players.get(0).getPrivateObjectiveCards();
        //TODO notify observer
    }

    @Override
    public int getNumberOfToolCardForGame() {
        return getDifficulty();
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
        int targetScore = getTargetScore();
        Outcome outcome = (scoreMap.get(currentRoundPlayer) > targetScore) ? Outcome.WIN : Outcome.LOSE;
        setPlayerOutcome(currentRoundPlayer, outcome);
    }

    @Override
    public boolean isSinglePlayer() {
        return true;
    }

    @Override
    public void addNewPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards) {
        setPlayer(token, schemaCard, privateObjectiveCards);
    }

    @Override
    public void notifyPlayersEndGame() {
        requestPrivateObjectiveCardFromPlayer();
    }
}
