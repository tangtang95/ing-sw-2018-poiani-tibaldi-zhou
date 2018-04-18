package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Iterator;

public class PrivateObjectiveCard extends Card implements IScore {

    private IConstraint colorConstraint;

    protected PrivateObjectiveCard(String name, String description) {
        super(name, description);
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        Tile[][] tileMatrix = schema.getTileMatrix();
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = tileMatrix[i][j].getDice();
                if(dice.getColorConstraint().matches(colorConstraint))
                    score += dice.getNumber();
            }
        }
        return score;
    }

    public IConstraint getConstraint(){
        return colorConstraint;
    }

}
