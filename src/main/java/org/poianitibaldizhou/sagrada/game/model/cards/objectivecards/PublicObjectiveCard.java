package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;
import org.poianitibaldizhou.sagrada.network.protocol.SharedConstants;

import java.util.*;

/**
 * OVERVIEW: represents a public objective card for the game. The objective can regard colors or numbers.
 * It calculates the point basing it self on this and on a certain level of card point, which are given to the card.
 */
@Immutable
public abstract class PublicObjectiveCard extends Card implements IScore, JSONable {

    protected final ObjectiveCardType type;
    protected transient Set<IConstraint> constraints;
    private final int cardPoints;

    /**
     * PublicObjectiveCard param for network protocol.
     */
    private static final String JSON_CARD_POINT = "cardPoint";

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a certain name, description and points.
     *
     * @param name        card's name
     * @param description card's description
     * @param cardPoints  card's point
     */
    public PublicObjectiveCard(final String name, final String description, final int cardPoints, final ObjectiveCardType type) {
        super(name, description);

        this.type = type;
        this.cardPoints = cardPoints;
    }

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a name, description and points.
     * This also requires the type of constraint on which the cards operate: a PublicObjectiveCard only deals
     * with a single PlacementRestrictionType.
     *
     * @param name        card's name
     * @param description card's description
     * @param cardPoints  card's point
     * @param constraints set of constraint to apply
     * @param type        type of tile constraint on which the card operates
     */
    public PublicObjectiveCard(String name, String description, int cardPoints, Collection<IConstraint> constraints, ObjectiveCardType type) {
        this(name, description, cardPoints, type);

        for (IConstraint constraint : constraints) {
            if (type == ObjectiveCardType.COLOR) {
                if (!(constraint instanceof ColorConstraint))
                    throw new IllegalArgumentException("constraints has different type than PlacementRestrictionType given");
            } else if (type == ObjectiveCardType.NUMBER) {
                if (!(constraint instanceof NumberConstraint))
                    throw new IllegalArgumentException("constraints has different type than PlacementRestrictionType given");
            } else {
                throw new IllegalArgumentException("cannot instantiate publicObjectiveCard with this PlacementRestrictionType");
            }
        }

        this.constraints = new HashSet<>();
        this.constraints.addAll(constraints);
    }


    public List<IConstraint> getConstraint() {
        return new ArrayList<>(this.constraints);
    }

    public ObjectiveCardType getType() {
        return type;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    /**
     * Checks if other is present in constraints.
     *
     * @param other IConstraint to check
     * @return if constraints contains other true, false otherwise
     */
    public boolean containsConstraint(IConstraint other) {
        for (IConstraint constraint : constraints) {
            if (constraint.equals(other)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert a publicObjectiveCard in a JSONObject.
     *
     * @return a JSONObject.
     */
    @Override
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject main = new JSONObject();
        JSONObject cardJSON = new JSONObject();
        cardJSON.put(JSON_NAME, this.getName());
        cardJSON.put(JSON_DESCRIPTION, this.getDescription());
        cardJSON.put(JSON_CARD_POINT, this.getCardPoints());
        main.put(SharedConstants.TYPE, SharedConstants.PUBLIC_OBJECTIVE_CARD);
        main.put(SharedConstants.BODY,cardJSON);
        return main;
    }

    /**
     * Convert a json string in a publicObjectiveCard object.
     *
     * @param jsonObject a JSONObject that contains a name of the publicObjectiveCard.
     * @return a publicObjectiveCard object or null if the jsonObject is wrong.
     */
    @Override
    public Object toObject(JSONObject jsonObject) {
        /*This method is empty because the client never send a publicObjectiveCard*/
        return null;
    }

    /**
     * Fake constructor.
     */
    @SuppressWarnings("unused")
    private PublicObjectiveCard(){
        super(null,null);
        this.type = null;
        this.cardPoints = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicObjectiveCard)) return false;
        PublicObjectiveCard that = (PublicObjectiveCard) o;
        return getCardPoints() == that.getCardPoints() &&
                getType() == that.getType() &&
                this.getName().equals(that.getName()) &&
                this.getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getType(), getCardPoints(), getName(), getDescription());
    }
}
