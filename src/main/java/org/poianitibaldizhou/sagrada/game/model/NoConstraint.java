package org.poianitibaldizhou.sagrada.game.model;

public class NoConstraint implements IConstraint{
    @Override
    public boolean matches(IConstraint other) {
        return true;
    }

    @Override
    public int getIndexValue() {
        return -1;
    }
}
