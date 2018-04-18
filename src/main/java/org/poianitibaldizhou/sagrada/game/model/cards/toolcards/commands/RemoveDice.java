package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;

public class RemoveDice implements ICommand {

    private ConstraintType constraintType;

    public RemoveDice(ConstraintType type) {
        this.constraintType = type;
    }

    @Override
    public void executeCommand(Player player) {

    }
}
