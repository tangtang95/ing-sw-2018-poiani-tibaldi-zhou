package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.ConstraintMixedException;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class DiagonalPublicObjectiveCard extends PublicObjectiveCard{

    protected DiagonalPublicObjectiveCard(String name, String description, Collection<IConstraint> constraints) throws ConstraintMixedException {
        super(name, description, constraints);
    }

    @Override
    public int getScore(SchemaCard schema) {
        return 0;
    }


    /*private int getDiagonalScore(Tile[][] tileMatrix, int row, int column){
        if(tileMatrix[row][column].getDice() == null)
            return 0;
        for (int deltaRow = -1; deltaRow <= 1; deltaRow++) {
            for (int deltaColumn = -1; deltaColumn <= 1 ; deltaColumn++) {
                if(Math.abs(deltaRow) + Math.abs(deltaColumn) == 2){
                    Dice dice = tileMatrix[row][column].getDice();
                    Dice other = tileMatrix[row+deltaRow][column+deltaColumn].getDice();
                    if(dice.getColorConstraint().matches(other.getColorConstraint()))
                        getDiagonalScore()
                }
            }

        }

    }*/

}
