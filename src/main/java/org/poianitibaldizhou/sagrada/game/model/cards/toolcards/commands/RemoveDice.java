package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;

public class RemoveDice implements ICommand {

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    private final ConstraintType constraintType;

    public RemoveDice(ConstraintType type) {
        this.constraintType = type;
    }

    @Override
    public void executeCommand(Player player) {

    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof RemoveDice))
            return false;

        RemoveDice obj = (RemoveDice)object;
        return obj.getConstraintType() == this.constraintType ? true:false;
    }
}
