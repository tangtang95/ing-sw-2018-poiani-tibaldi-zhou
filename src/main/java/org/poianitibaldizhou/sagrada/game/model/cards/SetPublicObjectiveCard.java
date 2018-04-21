package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintTypeException;
import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;
import java.util.List;

public class SetPublicObjectiveCard extends PublicObjectiveCard {

    protected SetPublicObjectiveCard(String name, String description, int cardPoints, Collection<IConstraint> constraints, TileConstraintType type) throws ConstraintTypeException {
        super(name, description, cardPoints, constraints, type);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int[] counts = new int[Math.max(Dice.MAX_VALUE, Color.values().length)];
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                try {
                    Dice dice = schema.getDice(new SchemaCardPoint(i,j));
                    if(dice != null) {
                        int indexValue;
                        if(getType() == TileConstraintType.COLOR)
                            indexValue = containsConstraint(dice.getColorConstraint());
                        else
                            indexValue = containsConstraint(dice.getNumberConstraint());
                        if (indexValue != -1)
                            counts[indexValue]++;
                    }
                } catch (SchemaCardPointOutOfBoundsException e) {
                    e.printStackTrace();
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
