package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.dice.DiceRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCardExecutorHelper;

import java.rmi.RemoteException;
import java.util.List;

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
     * @param player player that invoked the toolcard: its schema card will receive a new dice
     * @param toolCardExecutorHelper toolcard that has been invoked
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException given to
     * @throws RemoteException network communication error
     */
    @Override
    public boolean executeCommand(Player player, ToolCardExecutorHelper toolCardExecutorHelper, Game game) throws InterruptedException, RemoteException {
        Dice dice;
        SchemaCard schemaCard;
        Position position;
        boolean flag = false;

        dice = toolCardExecutorHelper.getNeededDice();
        schemaCard = player.getSchemaCard();

        do {
            List<IToolCardObserver> observerList = toolCardExecutorHelper.getObservers();
            for(IToolCardObserver obs : observerList)
                obs.notifyNeedPosition(player);

            position = toolCardExecutorHelper.getPosition();

            try {
                schemaCard.setDice(dice, position.getRow(), position.getColumn(), this.tileConstraint,
                        this.diceConstraint);
                flag = true;
            } catch (RuleViolationException e) {
                e.printStackTrace();
            }
        } while(!flag);

        return true;
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
