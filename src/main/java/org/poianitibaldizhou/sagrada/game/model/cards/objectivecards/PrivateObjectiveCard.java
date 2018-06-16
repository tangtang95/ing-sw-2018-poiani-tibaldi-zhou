package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.poianitibaldizhou.sagrada.game.model.Color;
import org.poianitibaldizhou.sagrada.game.model.GameInjector;
import org.poianitibaldizhou.sagrada.game.model.cards.Position;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.SchemaCard;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.board.Dice;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.network.protocol.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * OVERVIEW: Represents a private objective for a certain player.
 */
@Immutable
public class PrivateObjectiveCard extends Card implements IScore, JSONable {

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
                Dice dice = schema.getDice(new Position(i, j));
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(!(obj instanceof PrivateObjectiveCard))
            return false;
        PrivateObjectiveCard other = (PrivateObjectiveCard) obj;
        return getName().equals(other.getName())
                && getDescription().equals(other.getDescription())
                && getConstraint().equals(other.getConstraint());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getConstraint());
    }

    /**
     * Convert a privateObjectiveCard in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject pocJSON = new JSONObject();
        pocJSON.put(JSON_NAME, this.getName());
        pocJSON.put(JSON_DESCRIPTION, this.getDescription());
        pocJSON.put(JSON_COLOR,this.colorConstraint.getColor().name());
        main.put(SharedConstants.TYPE, SharedConstants.PRIVATE_OBJECTIVE_CARD);
        main.put(SharedConstants.BODY,pocJSON);
        return main;
    }

    /**
     * Convert a json string in a privateObjectiveCard object.
     *
     * @param jsonObject a JSONObject that contains a name of the privateObjectiveCard.
     * @return a privateObjectiveCard object or null if the jsonObject is wrong.
     */
    public static PrivateObjectiveCard toObject(JSONObject jsonObject) {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) jsonParser.parse(new FileReader("resources/privateObjectiveCards.json"));
            for (Object object : Objects.requireNonNull(jsonArray)) {
                JSONObject privateObjectiveCard = (JSONObject) object;
                if (privateObjectiveCard.get(GameInjector.CARD_NAME).toString().equals(jsonObject
                        .get(JSON_NAME).toString()))
                    return new PrivateObjectiveCard(
                            (String) privateObjectiveCard.get(GameInjector.CARD_NAME),
                            (String) privateObjectiveCard.get(GameInjector.CARD_DESCRIPTION),
                            Color.valueOf((String) privateObjectiveCard.get("cardColor")));
            }
        } catch (IOException | ParseException | NullPointerException e) {
            return null;
        }
        return null;
    }

}
