package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

@Immutable
public class DiagonalPublicObjectiveCard extends PublicObjectiveCard {

    /**
     * Constructor.
     * Creates a DiagonalPublicObjectiveCard with a name, description and points.
     * This also requires the type of objectiveCard (color or number) on which the cards operate
     *
     * @param name card's name
     * @param description card's description
     * @param cardPoints card's point
     * @param type type of tile constraint on which the card operates
     */
    public DiagonalPublicObjectiveCard(String name, String description, int cardPoints, ObjectiveCardType type) {
        super(name, description, cardPoints, type);
    }

    /**
     * Returns the score obtained by a SchemaCard following the rule implied by DiagonalPublicObjectiveCard.
     * Basically, for each dice inside the schemaCard which has at least one diagonal adjacent dice with the same
     * color (if ObjectiveCardType is Color type) the score increase by cardPoints
     *
     * @param schema SchemaCard on which DiagonalPublicObjectiveCard rules needs to be applied
     * @return score obtained
     */
    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(new Position(i, j));
                if (dice != null && getNumberOfSimilarDiceDiagonally(schema, dice, new Position(i, j)) > 0)
                    score += this.getCardPoints();
            }
        }
        return score;
    }

    /**
     * Return the number of similar dice (i.e. if ObjectiveCardType is Color type then a similar dice is a dice
     * with the same color) diagonally adjacent
     *
     *
     * @param schema SchemaCard on which DiagonalPublicObjectiveCard rules needs to be applied
     * @param dice the dice on the schemaCard analyzing
     * @param position the row position of the dice
     * @return number of similar dice
     */
    private int getNumberOfSimilarDiceDiagonally(SchemaCard schema, Dice dice, Position position) {
        int numberOfSimilarDices = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                if (Math.abs(deltaColumn) + Math.abs(deltaRow) == 2 &&
                        !SchemaCard.isOutOfBounds(position.getRow() + deltaRow, position.getColumn() + deltaColumn)) {
                    Dice other = schema.getDice(position.add(deltaRow, deltaColumn));
                    boolean isSimilar = (getType() == ObjectiveCardType.COLOR) ?
                            dice.hasSameColor(other) : dice.hasSameNumber(other);
                    numberOfSimilarDices += (isSimilar) ? 1 : 0;
                }
            }
        }
        return numberOfSimilarDices;
    }

}
