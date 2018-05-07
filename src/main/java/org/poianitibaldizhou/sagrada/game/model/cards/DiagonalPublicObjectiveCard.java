package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.game.model.Dice;

public class DiagonalPublicObjectiveCard extends PublicObjectiveCard {

    public DiagonalPublicObjectiveCard(String name, String description, int cardPoints, TileConstraintType type) {
        super(name, description, cardPoints, type);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(i, j);
                if (dice != null && hasSimilarDiceDiagonally(schema, dice, i, j))
                    score += this.getCardPoints();
            }
        }
        return score;
    }

    private boolean hasSimilarDiceDiagonally(SchemaCard schema, Dice dice, int row, int column) {
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1; deltaColumn++) {
                if (Math.abs(deltaColumn) + Math.abs(deltaRow) == 2 &&
                        !SchemaCard.isOutOfBounds(row + deltaRow, column + deltaColumn)) {
                    Dice other = schema.getDice(row + deltaRow, column + deltaColumn);
                    if(other != null) {
                        if (getType() == TileConstraintType.COLOR)
                            return dice.getColor() == other.getColor();
                        else
                            return dice.getNumber() == other.getNumber();
                    }
                }
            }
        }
        return false;
    }

}
