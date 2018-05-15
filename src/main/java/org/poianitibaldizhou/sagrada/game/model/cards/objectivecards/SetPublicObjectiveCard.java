package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;

import java.util.Collection;
import java.util.List;

@Immutable
public class SetPublicObjectiveCard extends PublicObjectiveCard {

    /**
     * Constructor.
     * Creates a SetPublicObjectiveCard with a name, description and points.
     * This also requires the type of constraint on which the cards operate: a PublicObjectiveCard only deals
     * with a single PlacementRestrictionType.
     *
     * @param name card's name
     * @param description card's description
     * @param cardPoints card's point
     * @param constraints set of constraint to apply
     * @param type type of tile constraint on which the card operates
     */
    public SetPublicObjectiveCard(String name, String description, int cardPoints,
                                  Collection<IConstraint> constraints, ObjectiveCardType type) {
        super(name, description, cardPoints, constraints, type);
    }

    /**
     * Returns the score obtained by a SchemaCard following the rule implied by SetPublicObjectiveCard.
     * Basically, in order to get some points (cardsPoint for each occurrence) there will need to be tuple
     * of dices with matching constraint of ColorConstraint or NumberConstraint, depending on this.type and
     * this.constraints. Every constraint presents in this.constraint needs to be matched once in the tuple.
     *
     * @param schema SchemaCard on which SetPublicObjectiveCard rules needs to be applied
     * @return score obtained
     */
    @Override
    public int getScore(SchemaCard schema) {
        int[] counts = new int[Math.max(Dice.MAX_VALUE, Color.values().length)];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(i,j);
                if(dice != null) {
                    IConstraint constraint = (getType() == ObjectiveCardType.COLOR) ?
                            dice.getColorConstraint() : dice.getNumberConstraint();
                    counts[constraint.getIndexValue()]++;
                }

            }
        }
        int minValue = Integer.MAX_VALUE;
        List<IConstraint> arrayConstraints = getConstraint();
        for (int i = 0; i < arrayConstraints.size(); i++) {
            minValue = Math.min(minValue, counts[arrayConstraints.get(i).getIndexValue()]);
        }
        return minValue*getCardPoints();
    }

}
