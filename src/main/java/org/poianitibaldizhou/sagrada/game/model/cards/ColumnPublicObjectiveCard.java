package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class ColumnPublicObjectiveCard extends PublicObjectiveCard{

    protected ColumnPublicObjectiveCard(String name, String description, int cardPoints,
                                        Collection<IConstraint> constraints, TileConstraintType type) throws ConstraintTypeException {
        super(name, description, cardPoints, constraints, type);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_COLUMNS; i++) {
            Set<Integer> valueSet = new TreeSet();
            for (int j = 0; j < SchemaCard.NUMBER_OF_ROWS; j++) {
                Dice dice = schema.getDice(new SchemaCardPoint(j,i));
                if(dice != null) {
                    if (getType() == TileConstraintType.COLOR)
                        valueSet.add(dice.getColorConstraint().getIndexValue());
                    else
                        valueSet.add(dice.getNumberConstraint().getIndexValue());
                }
            }
            if (valueSet.size() == SchemaCard.NUMBER_OF_ROWS)
                score += getCardPoints();
        }
        return score;
    }

}
