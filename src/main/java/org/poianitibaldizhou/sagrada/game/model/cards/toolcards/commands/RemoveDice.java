package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class RemoveDice implements ICommand {

    public PlacementRestrictionType getConstraintType() {
        return constraintType;
    }

    private final PlacementRestrictionType constraintType;

    /**
     * The constructor only allow no constraint or color constraint.
     *
     * @param type restriction that needs to be followed for removing the dice
     */
    public RemoveDice(PlacementRestrictionType type) {
        if (type != PlacementRestrictionType.NONE && type != PlacementRestrictionType.COLOR)
            throw new IllegalArgumentException("Type of restriction not allowed for this command");
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
     * @param player           player that invoked the command
     * @param toolCardExecutor toolcard invoked
     * @param turnState        state in which the player acts
     * @return CommandFlow.REPEAT if the specified position doesn't contain a dice or if the dice contain doesn't match
     * the specified color constraint. CommandFlow.STOP if it's not possible to remove a dice under the give
     * CommandFlow.MAIN otherwise.
     * @throws RemoteException      network communication error
     * @throws InterruptedException due to wait() in toolcard retrieving methods
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws RemoteException, InterruptedException {
        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        Position position;
        Dice removed = null;
        Color color;

        if (this.constraintType == PlacementRestrictionType.COLOR) {
            color = toolCardExecutor.getNeededColor();
            if(!(toolCardExecutor.getTemporarySchemaCard().hasDiceOfColor(color)))
                return CommandFlow.STOP;
            for (IToolCardExecutorObserver obs : observerList)
                obs.notifyNeedDicePositionOfCertainColor(color);
            position = toolCardExecutor.getPosition();
            if (!toolCardExecutor.getTemporarySchemaCard().getDice(position).getColor().equals(color)) {
                toolCardExecutor.setNeededPosition(null);
                return CommandFlow.REPEAT;
            }
        } else {
            if(toolCardExecutor.getTemporarySchemaCard().isEmpty())
                return CommandFlow.STOP;
            for (IToolCardExecutorObserver obs : observerList)
                obs.notifyNeedPosition();
            position = toolCardExecutor.getPosition();
        }

        removed = toolCardExecutor.getTemporarySchemaCard().removeDice(position);

        if (removed == null) {
            toolCardExecutor.setNeededPosition(null);
            return CommandFlow.REPEAT;
        }
        toolCardExecutor.setNeededDice(removed);
        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RemoveDice))
            return false;

        RemoveDice obj = (RemoveDice) object;
        return obj.getConstraintType() == this.constraintType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RemoveDice.class, getConstraintType());
    }
}
