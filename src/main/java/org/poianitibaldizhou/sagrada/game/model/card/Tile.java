package org.poianitibaldizhou.sagrada.game.model.card;

import org.poianitibaldizhou.sagrada.game.model.IConstraint;

public class Tile{

    private IConstraint constraint;

    public Tile(IConstraint constraint){
        this.constraint = constraint;
    }

    public IConstraint getConstraint() {
        return constraint;
    }
}
