package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.exception.ConstraintMixedException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class RowPublicObjectiveCard extends PublicObjectiveCard{

    private boolean isColorType;

    protected RowPublicObjectiveCard(String name, String description, Collection<IConstraint> constraints, boolean isColorType) throws ConstraintMixedException {
        super(name, description, constraints);
        this.isColorType = isColorType;
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        Tile[][] tileMatrix = schema.getTileMatrix();
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            Set<Integer> valueSet = new TreeSet();
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = tileMatrix[i][j].getDice();
                if(dice != null) {
                    if (isColorType)
                        valueSet.add(dice.getColorConstraint().getValue());
                    else
                        valueSet.add(dice.getNumberConstraint().getValue());
                }
            }
            if (valueSet.size() == SchemaCard.NUMBER_OF_COLUMNS)
                score += getCardPoints();
        }
        return score;
    }
}
