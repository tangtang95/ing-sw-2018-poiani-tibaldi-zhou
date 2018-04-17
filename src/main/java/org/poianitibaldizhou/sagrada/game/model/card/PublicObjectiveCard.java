package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.*;

public abstract class PublicObjectiveCard extends Card implements IScore {

    private Set<IConstraint> constraints;
    private int scorePoints;

    protected PublicObjectiveCard(String name, String description, Collection<IConstraint> constraints) {
        super(name, description);
        this.constraints = new TreeSet();
        this.constraints.addAll(constraints);
    }

    public List<IConstraint> getConstraint() {
        return new ArrayList(constraints);
    }

    public int getScorePoints() {
        return scorePoints;
    }

}
