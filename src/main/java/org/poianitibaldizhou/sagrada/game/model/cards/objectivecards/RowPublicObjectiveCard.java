package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.HashSet;
import java.util.Set;

@Immutable
public class RowPublicObjectiveCard extends PublicObjectiveCard{

    /**
     * Constructor.
     * Creates a RowPublicObjectiveCard with a name, description and points.
     * This also requires the type of objectiveCard (color or number) on which the cards operate:
     * a PublicObjectiveCard only deals with a single type.
     *  @param name card's name
     * @param description card's description
     * @param cardPoints card's point
     * @param type type of tile constraint on which the card operates
     */
    public RowPublicObjectiveCard(String name, String description, int cardPoints, ObjectiveCardType type)  {
        super(name, description, cardPoints, type);

        constraints = new HashSet<>();
        constraints.addAll((type == ObjectiveCardType.COLOR) ?
                ColorConstraint.getAllColorConstraints() : NumberConstraint.getAllNumberConstraint());
    }


    /**
     * Returns the score obtained by a SchemaCard following the rule implied by RowPublicObjectiveCard.
     * Basically, in order to get some points (cardsPoint for each row) there will need to be a row
     * with different constraint of ColorConstraint or NumberConstraint, depending on this.type.
     *
     * @param schema SchemaCard on which RowPublicObjectiveCard rules needs to be applied
     * @return score obtained
     */
    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            Set<Integer> valueSet = new HashSet<>();
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(new Position(i, j));
                if (dice != null) {
                    IConstraint constraint = (getType() == ObjectiveCardType.COLOR) ?
                            dice.getColorConstraint() : dice.getNumberConstraint();
                    valueSet.add(constraint.getIndexValue());
                }
            }
            if (valueSet.size() == SchemaCard.NUMBER_OF_COLUMNS)
                score += getCardPoints();
        }
        return score;
    }

}
