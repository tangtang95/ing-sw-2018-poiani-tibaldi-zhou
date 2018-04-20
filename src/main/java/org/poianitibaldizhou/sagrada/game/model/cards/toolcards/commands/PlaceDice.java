package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.ConstraintType;

public class PlaceDice implements ICommand {

    public final ConstraintType constraintType;

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public PlaceDice(ConstraintType constraintType) {
        this.constraintType = constraintType;

    }

    @Override
    public void executeCommand(Player player) {

    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof PlaceDice))
            return false;

        PlaceDice obj = (PlaceDice) object;
        return obj.getConstraintType() == this.constraintType? true:false;
    }
}
