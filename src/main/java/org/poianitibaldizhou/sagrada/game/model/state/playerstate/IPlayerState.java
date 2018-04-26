package org.poianitibaldizhou.sagrada.game.model.state.playerstate;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

public interface IPlayerState {
    void chooseAction(String action);
    void useCard(Player player, ToolCard toolCard);
    void placeDice(Player player, Dice dice);
}
