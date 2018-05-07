package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

public class PlaceDice implements ICommand {

    public final TileConstraintType tileConstraint;
    public final DiceConstraintType diceConstraint;

    public PlaceDice(TileConstraintType tileConstraint, DiceConstraintType diceConstraint) {
        this.tileConstraint = tileConstraint;
        this.diceConstraint = diceConstraint;

    }

    public TileConstraintType getTileConstraint() {
        return tileConstraint;
    }

    public DiceConstraintType getDiceConstraint() {
        return diceConstraint;
    }

    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {

    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof PlaceDice))
            return false;

        PlaceDice obj = (PlaceDice) object;
        return obj.getTileConstraint() == this.getTileConstraint()
                && obj.getDiceConstraint() == this.getDiceConstraint();
    }
}
