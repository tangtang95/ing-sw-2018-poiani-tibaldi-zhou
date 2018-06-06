package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.HashSet;
import java.util.Set;

/**
 * OVERVIEW: It calculates the point of a schema card following the rules that there will need to be a column
 * with different constraint of ColorConstraint or NumberConstraint.
 */
@Immutable
public class ColumnPublicObjectiveCard extends PublicObjectiveCard{

    /**
     * Constructor.
     * Creates a ColumnPublicObjectiveCard with a name, description and points.
     * This also requires the type of objectiveCard (color or number) on which the cards operate:
     * a PublicObjectiveCard only deals with a single type.
     *
     * @param name card's name
     * @param description card's description
     * @param cardPoints card's point
     * @param type type of tile constraint on which the card operates
     */
    public ColumnPublicObjectiveCard(String name, String description, int cardPoints,
                                     ObjectiveCardType type) {
        super(name, description, cardPoints, type);

        constraints = new HashSet<>();
        constraints.addAll((type == ObjectiveCardType.COLOR) ?
                ColorConstraint.getAllColorConstraints() : NumberConstraint.getAllNumberConstraint());
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
        for (int col = 0; col < SchemaCard.NUMBER_OF_COLUMNS; col++) {
            Set<Integer> valueSet = new HashSet<>();
            for (int row = 0; row < SchemaCard.NUMBER_OF_ROWS; row++) {
                Dice dice = schema.getDice(new Position(row, col));
                if(dice != null) {
                    IConstraint constraint = (getType() == ObjectiveCardType.COLOR) ?
                            dice.getColorConstraint() : dice.getNumberConstraint();
                    valueSet.add(constraint.getIndexValue());
                }
            }
            if (valueSet.size() == SchemaCard.NUMBER_OF_ROWS)
                score += this.getCardPoints();
        }
        return score;
    }

}
