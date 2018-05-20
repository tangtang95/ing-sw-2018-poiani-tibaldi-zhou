package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.*;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.state.IStateGame;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class PlaceDice implements ICommand {

    public final PlacementRestrictionType tileConstraint;
    public final DiceRestrictionType diceConstraint;

    /**
     * Constructor.
     * Creates a command that has the purpose to place a dice in the player's SchemaCard.
     *
     * @param tileConstraint TileConstraint that need to be checked when placing the dice
     * @param diceConstraint DiceConstraint that need to be checked when placing the dice
     */
    public PlaceDice(PlacementRestrictionType tileConstraint, DiceRestrictionType diceConstraint) {
        this.tileConstraint = tileConstraint;
        this.diceConstraint = diceConstraint;
    }

    public PlacementRestrictionType getTileConstraint() {
        return tileConstraint;
    }

    public DiceRestrictionType getDiceConstraint() {
        return diceConstraint;
    }

    /**
     * Place a dice following the constraint given to the command.
     * The dice is placed in the schema card of a specified player.
     * This method requires a dice in toolcard. It will ask for a position to the client.
     *
     * @param player           player that invoked the toolcard: its schema card will receive a new dice
     * @param toolCardExecutor toolcard that has been invoked
     * @param stateGame
     * @return CommandFlow.REPEAT if the restrictions aren't respected; CommandFlow.STOP if it's not possibile to place
     * the dice in any position; CommandFlow.MAIN otherwise
     * @throws InterruptedException given to wait() in getting parameters from the executor
     * @throws RemoteException      network communication error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, IStateGame stateGame) throws RemoteException, InterruptedException {
        Dice dice;
        Position position;

        dice = toolCardExecutor.getNeededDice();

        if(!(toolCardExecutor.getTemporarySchemaCard().isDicePositionable(dice, tileConstraint, diceConstraint))) {
            return CommandFlow.STOP;
        }

        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        for (IToolCardExecutorObserver obs : observerList)
            obs.notifyNeedPosition();

        position = toolCardExecutor.getPosition();

        try {
            toolCardExecutor.getTemporarySchemaCard().setDice(dice, position.getRow(), position.getColumn(), this.tileConstraint, this.diceConstraint);
        } catch (RuleViolationException e) {
            return CommandFlow.REPEAT;
        }

        return CommandFlow.MAIN;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlaceDice))
            return false;

        PlaceDice obj = (PlaceDice) object;
        return obj.getTileConstraint() == this.getTileConstraint()
                && obj.getDiceConstraint() == this.getDiceConstraint();
    }

    @Override
    public int hashCode() {
        return Objects.hash(PlaceDice.class, getTileConstraint(), getDiceConstraint());
    }
}
