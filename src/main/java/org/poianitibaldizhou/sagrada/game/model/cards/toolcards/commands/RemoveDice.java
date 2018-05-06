package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;

public class RemoveDice implements ICommand {

    public TileConstraintType getConstraintType() {
        return constraintType;
    }

    private final TileConstraintType constraintType;

    public RemoveDice(TileConstraintType type) {
        this.constraintType = type;
    }

    @Override
    public void executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException {

    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof RemoveDice))
            return false;

        RemoveDice obj = (RemoveDice)object;
        return obj.getConstraintType() == this.constraintType;
    }
}
