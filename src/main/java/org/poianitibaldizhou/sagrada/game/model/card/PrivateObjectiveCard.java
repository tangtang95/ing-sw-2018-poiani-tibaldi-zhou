package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.IConstraint;

public class PrivateObjectiveCard extends Card implements IScore {

    private IConstraint colorConstraint;

    protected PrivateObjectiveCard(String name, String description) {
        super(name, description);
    }

    @Override
    public int getScore(SchemaCard schema) {
        return 0;
    }

    public IConstraint getConstraint(){
        return colorConstraint;
    }

}
