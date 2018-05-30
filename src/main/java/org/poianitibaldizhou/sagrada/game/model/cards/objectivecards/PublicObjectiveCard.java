package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.json.simple.JSONObject;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.placement.PlacementRestrictionType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;
import org.poianitibaldizhou.sagrada.game.model.observers.fakeobservers.JSONable;

import java.io.ObjectInputStream;
import java.util.*;

@Immutable
public abstract class PublicObjectiveCard extends Card implements IScore, JSONable {

    protected final ObjectiveCardType type;
    protected Set<IConstraint> constraints;
    private final int cardPoints;

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a certain name, description and points.
     *
     * @param name        card's name
     * @param description card's description
     * @param cardPoints  card's point
     */
    PublicObjectiveCard(final String name, final String description, final int cardPoints, final ObjectiveCardType type) {
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
    PublicObjectiveCard(String name, String description, int cardPoints, Collection<IConstraint> constraints, ObjectiveCardType type) {
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
        List<IConstraint> constraints = new ArrayList<>();
        constraints.addAll(this.constraints);
        return constraints;
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

    public JSONObject toJSON() {
        JSONObject cardJSON = new JSONObject();
        cardJSON.put("name", this.getName());
        cardJSON.put("description", this.getDescription());
        cardJSON.put("cardPoint", this.getCardPoints());
        return cardJSON;
    }
}
