package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.IConstraint;

import java.util.Collection;

public class ColumnPublicObjectiveCard extends PublicObjectiveCard{

    protected ColumnPublicObjectiveCard(String name, String description, Collection<IConstraint> constraints) {
        super(name, description, constraints);
    }

    @Override
    public int getScore(SchemaCard schema) {
        return 0;
    }

}
