package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class SetPublicObjectiveCard extends PublicObjectiveCard {

    protected SetPublicObjectiveCard(String name, String description, Collection<NumberConstraint> constraints) {
        super(name, description);
        this.constraints = new TreeSet();
        this.constraints.addAll(constraints);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int[] counts = new int[Dice.MAX_VALUE];
        Tile[][] tileMatrix = schema.getTileMatrix();
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = tileMatrix[i][j].getDice();
                if(dice != null) {
                    int diceNumber = containsConstraint(dice.getNumberConstraint());
                    if (diceNumber != -1) {
                        counts[diceNumber]++;
                    }
                }
            }
        }
        int minValue = Integer.MAX_VALUE;
        List<IConstraint> arrayConstraints = getConstraint();
        for (int i = 0; i < arrayConstraints.size(); i++) {
            minValue = Math.min(minValue, counts[arrayConstraints.get(i).getValue()]);
        }
        return minValue*getCardPoints();
    }
}
