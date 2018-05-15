package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;

@Immutable
public class DiagonalPublicObjectiveCard extends PublicObjectiveCard {

    public DiagonalPublicObjectiveCard(String name, String description, int cardPoints, ObjectiveCardType type) {
        super(name, description, cardPoints, type);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(i, j);
                if (dice != null && getNumberOfSimilarDiceDiagonally(schema, dice, i, j) > 0)
                    score += this.getCardPoints();
            }
        }
        return score;
    }

    private int getNumberOfSimilarDiceDiagonally(SchemaCard schema, Dice dice, int row, int column) {
        int numberOfSimilarDices = 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                if (Math.abs(deltaColumn) + Math.abs(deltaRow) == 2 &&
                        !SchemaCard.isOutOfBounds(row + deltaRow, column + deltaColumn)) {
                    Dice other = schema.getDice(row + deltaRow, column + deltaColumn);
                    boolean isSimilar = (getType() == ObjectiveCardType.COLOR) ?
                            dice.hasSameColor(other) : dice.hasSameNumber(other);
                    numberOfSimilarDices += (isSimilar) ? 1 : 0;
                }
            }
        }
        return numberOfSimilarDices;
    }

    public static DiagonalPublicObjectiveCard newInstance(DiagonalPublicObjectiveCard dpoc) {
        if (dpoc == null)
            return null;
        return new DiagonalPublicObjectiveCard(dpoc.getName(),dpoc.getDescription(),dpoc.getCardPoints(),
                dpoc.getType());
    }

}
