package org.poianitibaldizhou.sagrada.game.model;

import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.objectivecards.PrivateObjectiveCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands.ICommand;

import java.util.List;
import java.util.Map;

public interface IGameStrategy {

    int getNumberOfToolCardForGame();
    int getNumberOfPublicObjectiveCardForGame();
    int getNumberOfDicesToDraw();
    int getNumberOfPrivateObjectiveCardForGame();
    void setPlayersOutcome(Map<Player, Integer> scoreMap, Player currentRoundPlayer);
    boolean isSinglePlayer();
    void addNewPlayer(String token, SchemaCard schemaCard, List<PrivateObjectiveCard> privateObjectiveCards);
    void notifyPlayersEndGame();
    Node<ICommand> getCompleteCommands(ToolCard toolCard);
}
