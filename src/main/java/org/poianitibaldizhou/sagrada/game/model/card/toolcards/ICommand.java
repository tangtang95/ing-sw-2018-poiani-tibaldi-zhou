package org.poianitibaldizhou.sagrada.game.model.card.toolcards;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;

public interface ICommand {
    public void executeCommand(Player player);
}
