package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ColumnPublicObjectiveCard extends PublicObjectiveCard{

    /**
     * Constructor.
     * Creates a ColumnPublicObjectiveCard with a name, description and points.
     * This also requires the type of constraint on which the cards operate: a PublicObjectiveCard only deals
     * with a single TileConstraintType.
     *
     * @param name card's name
     * @param description card's description
     * @param cardPoints card's point
     * @param constraints set of constraint to apply
     * @param type type of tile constraint on which the card operates
     */
    public ColumnPublicObjectiveCard(String name, String description, int cardPoints,
                                        Collection<IConstraint> constraints, TileConstraintType type) throws IllegalArgumentException {
        super(name, description, cardPoints, constraints, type);
    }

    /**
     * Returns the score obtained by a SchemaCard following the rule implied by ColumnPublicObjectiveCard.
     * Basically, in order to get some points (cardsPoint for each column) there will need to be a column
     * with different constraint of ColorConstraint or NumberConstraint, depending on this.type.
     *
     * @param schema SchemaCard on which ColumnPublicObjectiveCard rules needs to be applied
     * @return score obtained
     */
    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            Set<Integer> valueSet = new HashSet<>();
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                Dice dice = schema.getDice(j,i);
                if(dice != null) {
                    if (getType() == TileConstraintType.COLOR)
                        valueSet.add(dice.getColorConstraint().getIndexValue());
                    else
                        valueSet.add(dice.getNumberConstraint().getIndexValue());
                }
            }
            if (valueSet.size() == SchemaCard.NUMBER_OF_ROWS)
                score += this.getCardPoints();
        }
        return score;
    }
}
