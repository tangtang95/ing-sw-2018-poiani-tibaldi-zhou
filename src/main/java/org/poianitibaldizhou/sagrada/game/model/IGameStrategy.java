package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.exception.InvalidActionException;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;

import java.util.List;
import java.util.Map;

public interface IGameStrategy {

    int getNumberOfToolCardForGame();
    int getNumberOfPublicObjectiveCardForGame();
    int getPlayerScore(Player player);
    int getNumberOfDicesToDraw();
    int getNumberOfPrivateObjectiveCardForGame();
    void selectPrivateObjectiveCard(PrivateObjectiveCard privateObjectiveCard) throws InvalidActionException;
    void setPrivateObjectiveCard(Player player, DrawableCollection<PrivateObjectiveCard> privateObjectiveCards);

    void setPlayersOutcome(Game game, Map<Player, Integer> scoreMap, Player currentRoundPlayer);
}
