package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class RemoveDice implements ICommand {

    public PlacementRestrictionType getConstraintType() {
        return constraintType;
    }

    private final PlacementRestrictionType constraintType;

    public RemoveDice(PlacementRestrictionType type) {
        this.constraintType = type;
    }

    /**
     * Removes a dice from the toolcard.
     * If a color constraint is specified in this command class, the dice that needs to be removed must be
     * of the same color of toolcard's needed color, otherwise it can be any dice.
     * So it requires a color in constraintType == PlacementRestrictionType.COLOR.
     * Generally speaking, it doesn't require anything more and it will ask the client for a position and
     * it will remove the dice in that position.
     * It will push the removed dice in toolcard
     *
     * @param player player that invoked the command
     * @param toolCardExecutor toolcard invoked
     * @param game game in which the player acts
     * @return true
     * @throws RemoteException network communication error
     * @throws InterruptedException due to wait() in toolcard retrieving methods
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, Game game) throws RemoteException, InterruptedException {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        Position position;
        Dice removed = null;
        Color color;

        do {
            if (this.constraintType == PlacementRestrictionType.COLOR) {
                do {
                    color = toolCardExecutor.getNeededColor();
                    for(IToolCardExecutorObserver obs : observerList)
                        obs.notifyNeedDicePositionOfCertainColor(color);
                    position = toolCardExecutor.getPosition();

                } while (!player.getSchemaCard().getDice(position.getRow(), position.getColumn()).getColor().equals(color));
            } else {
                for (IToolCardExecutorObserver obs : observerList)
                    obs.notifyNeedPosition();
                position = toolCardExecutor.getPosition();
            }

            removed = game.removeDiceFromSchemaCardPlayer(player, position.getRow(), position.getColumn());
        } while(removed == null);
        toolCardExecutor.setNeededDice(removed);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof RemoveDice))
            return false;

        RemoveDice obj = (RemoveDice)object;
        return obj.getConstraintType() == this.constraintType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveDice.class, getConstraintType());
    }
}
