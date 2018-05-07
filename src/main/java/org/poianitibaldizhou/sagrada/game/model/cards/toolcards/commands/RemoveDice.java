package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import javafx.geometry.Pos;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class RemoveDice implements ICommand {

    public TileConstraintType getConstraintType() {
        return constraintType;
    }

    private final TileConstraintType constraintType;

    public RemoveDice(TileConstraintType type) {
        this.constraintType = type;
    }

    /**
     * Removes a dice from the toolcard.
     * If a color constraint is specified in this command class, the dice that needs to be removed must be
     * of the same color of toolcard's needed color, otherwise it can be any dice.
     *
     * @param player player that invoked the command
     * @param toolCard toolcard invoked
     * @param game game in which the player acts
     * @return true
     * @throws RemoteException network communication error
     * @throws InterruptedException due to wait() in toolcard retrieving methods
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws RemoteException, InterruptedException {
        SchemaCard schemaCard = player.getSchemaCard();
        List<IToolCardObserver> observerList = toolCard.getObservers();
        Position position;
        Dice removed = null;
        Color color;

        do {
            if (this.constraintType == TileConstraintType.COLOR) {
                do {
                    color = toolCard.getNeededColor();
                    for (IToolCardObserver obs : observerList)
                        obs.notifyNeedDicePositionOfCertainColor(player, color);
                    position = toolCard.getPosition();

                } while (!schemaCard.getDice(position.getRow(), position.getColumn()).getColor().equals(color));
            } else {
                for (IToolCardObserver obs : observerList)
                    obs.notifyNeedPosition(player);
                position = toolCard.getPosition();
            }

            removed = schemaCard.removeDice(position.getRow(), position.getColumn());
        } while(removed == null);
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof RemoveDice))
            return false;

        RemoveDice obj = (RemoveDice)object;
        return obj.getConstraintType() == this.constraintType;
    }
}
