package org.poianitibaldizhou.sagrada.game.model;

import org.jetbrains.annotations.Contract;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;

public class SinglePlayerGame extends Game {

    private final int difficulty;

    public SinglePlayerGame(String singlePlayerToken, int difficulty) {
        super("Single Player Game", singlePlayerToken);
        this.difficulty = difficulty;
        this.gameStrategy = new SinglePlayerGameStrategy(this);
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
}
