package org.poianitibaldizhou.sagrada.game.model.cards.objectivecards;

import jdk.nashorn.internal.ir.annotations.Immutable;
import org.poianitibaldizhou.sagrada.game.model.cards.Card;
import org.poianitibaldizhou.sagrada.game.model.cards.restriction.ObjectiveCardType;
import org.poianitibaldizhou.sagrada.game.model.constraint.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.constraint.NumberConstraint;

import java.util.*;

@Immutable
public abstract class PublicObjectiveCard extends Card implements IScore {

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
    PublicObjectiveCard(String name, String description, int cardPoints, ObjectiveCardType type) {
        super(name, description);

        this.type = type;
        this.cardPoints = cardPoints;
    }

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a name, description and points.
     * This also requires the type of constraint on which the cards operate: a PublicObjectiveCard only deals
     * with a single ObjectiveCardType.
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
                    throw new IllegalArgumentException("constraints has different type than ObjectiveCardType given");
            } else if (type == ObjectiveCardType.NUMBER) {
                if (!(constraint instanceof NumberConstraint))
                    throw new IllegalArgumentException("constraints has different type than ObjectiveCardType given");
            } else {
                throw new IllegalArgumentException("cannot instantiate publicObjectiveCard with this ObjectiveCardType");
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

    public static PublicObjectiveCard newInstance(PublicObjectiveCard publicObjectiveCard) {
        if (publicObjectiveCard instanceof RowPublicObjectiveCard)
            return RowPublicObjectiveCard.newInstance(publicObjectiveCard);
        if (publicObjectiveCard instanceof ColumnPublicObjectiveCard)
            return ColumnPublicObjectiveCard.newInstance(publicObjectiveCard);
        if (publicObjectiveCard instanceof DiagonalPublicObjectiveCard)
            return DiagonalPublicObjectiveCard.newInstance(publicObjectiveCard);
        if (publicObjectiveCard instanceof SetPublicObjectiveCard)
            return SetPublicObjectiveCard.newInstance(publicObjectiveCard);
        return null;
    }
}
