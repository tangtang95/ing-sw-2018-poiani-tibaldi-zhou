package org.poianitibaldizhou.sagrada.game.model.cards;

import org.poianitibaldizhou.sagrada.game.model.ColorConstraint;
import org.poianitibaldizhou.sagrada.game.model.IConstraint;
import org.poianitibaldizhou.sagrada.game.model.NumberConstraint;

import java.util.*;

public abstract class PublicObjectiveCard extends Card implements IScore {

    private final TileConstraintType type;
    private Set<IConstraint> constraints;
    private final int cardPoints;

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a certain name, description and points.
     *
     * @param name        card's name
     * @param description card's description
     * @param cardPoints  card's point
     */
    public PublicObjectiveCard(String name, String description, int cardPoints) {
        super(name, description);
        type = TileConstraintType.NONE;
        this.cardPoints = cardPoints;
    }

    /**
     * Constructor.
     * Creates a PublicObjectiveCard with a name, description and points.
     * This also requires the type of constraint on which the cards operate: a PublicObjectiveCard only deals
     * with a single TileConstraintType.
     *
     * @param name        card's name
     * @param description card's description
     * @param cardPoints  card's point
     * @param constraints set of constraint to apply
     * @param type        type of tile constraint on which the card operates
     */
    public PublicObjectiveCard(String name, String description, int cardPoints, Collection<IConstraint> constraints, TileConstraintType type) {
        super(name, description);

        for (IConstraint constraint : constraints) {
            if (type == TileConstraintType.COLOR) {
                if (!(constraint instanceof ColorConstraint))
                    throw new IllegalArgumentException();
            } else if (type == TileConstraintType.NUMBER) {
                if (!(constraint instanceof NumberConstraint))
                    throw new IllegalArgumentException();
            } else {
                throw new IllegalArgumentException();
            }
        }

        this.type = type;
        this.cardPoints = cardPoints;
        this.constraints = new HashSet<>();
        this.constraints.addAll(constraints);
    }


    public List<IConstraint> getConstraint() {
        return new ArrayList(constraints);
    }

    public TileConstraintType getType() {
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
}
