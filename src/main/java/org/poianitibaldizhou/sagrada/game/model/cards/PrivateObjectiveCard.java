package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.exception.SchemaCardPointOutOfBoundsException;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;


public class PrivateObjectiveCard extends Card implements IScore {

    private IConstraint colorConstraint;

    protected PrivateObjectiveCard(String name, String description, ColorConstraint colorConstraint) {
        super(name, description);
        this.colorConstraint = colorConstraint;
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                try {
                    Dice dice = schema.getDice(new SchemaCardPoint(i,j));
                    if(dice.getColorConstraint().matches(colorConstraint))
                        score += dice.getNumber();
                } catch (SchemaCardPointOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        return score;
    }

    public IConstraint getConstraint(){
        return colorConstraint;
    }

}
