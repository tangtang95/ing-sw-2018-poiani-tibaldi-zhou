package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.Dice;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;


public class PrivateObjectiveCard extends Card implements IScore {

    private ColorConstraint colorConstraint;

    /**
     * Constructor.
     * Creates a PrivateObjectiveCard with a certain name, description and a ColorConstraint.
     *
     * @param name
     * @param description
     * @param colorConstraint
     */
    public PrivateObjectiveCard(String name, String description, ColorConstraint colorConstraint) {
        super(name, description);
        this.colorConstraint = colorConstraint;
    }


    public PrivateObjectiveCard(String name, String description, Color color) {
        this(name, description, new ColorConstraint(color));
    }

    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(new SchemaCardPoint(i,j));
                if(dice != null) {
                    if (dice.getColorConstraint().equals(colorConstraint))
                        score += dice.getNumber();
                }
            }
        }
        return score;
    }

    public IConstraint getConstraint(){
        return colorConstraint;
    }

}
