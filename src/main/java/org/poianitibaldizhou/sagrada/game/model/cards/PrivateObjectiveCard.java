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
     * Each PrivateObjectiveCard defines rules on which calculate additional score based
     * on a certain color.
     *
     * @param name card's name
     * @param description card's description
     * @param colorConstraint card's color on which calculate additional score
     */
    public PrivateObjectiveCard(String name, String description, ColorConstraint colorConstraint) {
        super(name, description);
        this.colorConstraint = colorConstraint;
    }

    /**
     * Constructor.
     * Creates a PrivateObjectiveCard with a certain name, description and color.
     * Each PrivateObjectiveCard defines a rule based on a certain color on which calculate
     * additional score.
     * Example:
     * "Sum the values of the green dices"
     *
     * @param name card's name
     * @param description card's description
     * @param color card's color on which calculate additional score
     */
    public PrivateObjectiveCard(String name, String description, Color color) {
        this(name, description, new ColorConstraint(color));
    }

    /**
     * Calculates score on a rule that would be like "Sum the values of the green dices".
     * This operation is done looking at a SchemaCard.
     *
     * @param schema SchemaCard on which to perform the operation
     * @return the score calculated using the color of the PrivateObjectiveCard and the dices
     *         present on schema.
     */
    @Override
    public int getScore(SchemaCard schema) {
        int score = 0;
        for (int i = 0; i < SchemaCard.NUMBER_OF_ROWS; i++) {
            for (int j = 0; j < SchemaCard.NUMBER_OF_COLUMNS; j++) {
                Dice dice = schema.getDice(i,j);
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
