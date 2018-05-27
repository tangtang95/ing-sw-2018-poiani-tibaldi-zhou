package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.CommandFlow;
import org.poianitibaldizhou.sagrada.game.model.observers.IToolCardExecutorObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.executor.ToolCardExecutor;
import org.poianitibaldizhou.sagrada.game.model.players.Player;
import org.poianitibaldizhou.sagrada.game.model.state.TurnState;
import org.poianitibaldizhou.sagrada.game.model.state.playerstate.actions.PlaceDiceAction;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class PlaceDice implements ICommand {

    private final PlacementRestrictionType tileConstraint;
    private final DiceRestrictionType diceConstraint;
    private final boolean isNewPlacement;


    /**
     * Constructor.
     * Creates a command that has the purpose to place a dice in the player's SchemaCard.
     *
     * @param diceConstraint DiceConstraint that need to be checked when placing the dice
     * @param tileConstraint TileConstraint that need to be checked when placing the dice
     * @param isNewPlacement if it is a new placement on the schemaCard and not a movement
     */
    public PlaceDice(DiceRestrictionType diceConstraint, PlacementRestrictionType tileConstraint, boolean isNewPlacement) {
        this.tileConstraint = tileConstraint;
        this.diceConstraint = diceConstraint;
        this.isNewPlacement = isNewPlacement;
    }

    public PlacementRestrictionType getTileConstraint() {
        return tileConstraint;
    }

    public DiceRestrictionType getDiceConstraint() {
        return diceConstraint;
    }

    public boolean isNewPlacement(){ return isNewPlacement; }

    /**
     * Place a dice following the constraint given to the command.
     * The dice is placed in the schema card of a specified player.
     * This method requires a dice in toolcard. It will ask for a position to the client.
     *
     * @param player           player that invoked the toolCard: its schema card will receive a new dice
     * @param toolCardExecutor toolCard that has been invoked
     * @param turnState        the state of the game
     * @return CommandFlow.REPEAT if the restrictions aren't respected; CommandFlow.DICE_CANNOT_BE_PLACED_ANYWHERE if it's not possible to place
     * the dice in any position; CommandFlow.MAIN otherwise
     * @throws InterruptedException given to wait() in getting parameters from the executor
     * @throws RemoteException      network communication error
     */
    @Override
    public CommandFlow executeCommand(Player player, ToolCardExecutor toolCardExecutor, TurnState turnState) throws InterruptedException {
        if(isNewPlacement && turnState.hasActionUsed(new PlaceDiceAction()))
            return CommandFlow.PLACEMENT_ALREADY_DONE;

        Dice dice;
        Position position;

        dice = toolCardExecutor.getNeededDice();

        if (!(toolCardExecutor.getTemporarySchemaCard().isDicePositionable(dice, tileConstraint, diceConstraint))) {
            return CommandFlow.DICE_CANNOT_BE_PLACED_ANYWHERE;
        }

        List<IToolCardExecutorObserver> observerList = toolCardExecutor.getObservers();
        observerList.forEach(obs -> {
            try {
                obs.notifyNeedPosition();
            } catch (RemoteException e) {
                observerList.remove(obs);
            }
        });

        position = toolCardExecutor.getPosition();

        try {
            toolCardExecutor.getTemporarySchemaCard().setDice(dice, position, this.tileConstraint, this.diceConstraint);
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
                && obj.getDiceConstraint() == this.getDiceConstraint()
                && obj.isNewPlacement() == this.isNewPlacement();
    }

    @Override
    public int hashCode() {
        return Objects.hash(PlaceDice.class, getTileConstraint(), getDiceConstraint(), isNewPlacement());
    }
}
