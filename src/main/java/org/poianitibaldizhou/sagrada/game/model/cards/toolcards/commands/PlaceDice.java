package org.poianitibaldizhou.sagrada.game.model.cards.toolcards.commands;

import javafx.geometry.Pos;
import org.poianitibaldizhou.sagrada.exception.RuleViolationException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Game;
import org.poianitibaldizhou.sagrada.game.model.Player;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.DiceConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.cards.TileConstraintType;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.IToolCardObserver;
import org.poianitibaldizhou.sagrada.game.model.cards.toolcards.ToolCard;

import java.rmi.RemoteException;
import java.util.List;

public class PlaceDice implements ICommand {

    public final TileConstraintType tileConstraint;
    public final DiceConstraintType diceConstraint;

    /**
     * Constructor.
     * Creates a command that has the purpose to place a dice in the player's SchemaCard.
     *
     * @param tileConstraint TileConstraint that need to be checked when placing the dice
     * @param diceConstraint DiceConstraint that need to be checked when placing the dice
     */
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

    /**
     * Place a dice following the constraint given to the command.
     * The dice is placed in the schema card of a specified player.
     *
     * @param player player that invoked the toolcard: its schema card will receive a new dice
     * @param toolCard toolcard that has been invoked
     * @param game game in which the player acts
     * @return true
     * @throws InterruptedException given to
     * @throws RemoteException network communication error
     */
    @Override
    public boolean executeCommand(Player player, ToolCard toolCard, Game game) throws InterruptedException, RemoteException {
        Dice dice;
        SchemaCard schemaCard;
        Position position;
        boolean flag = false;

        dice = toolCard.getNeededDice();
        schemaCard = player.getSchemaCard();

        do {
            List<IToolCardObserver> observerList = toolCard.getObservers();
            for(IToolCardObserver obs : observerList)
                obs.notifyNeedPosition(player);

            position = toolCard.getPosition();

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
