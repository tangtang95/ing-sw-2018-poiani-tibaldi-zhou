package org.poianitibaldizhou.sagrada.game.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class ColorConstraint implements IConstraint {
    private final Color color;

    public ColorConstraint(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean matches(IConstraint other) {
        if(other instanceof NoConstraint)
            return true;
        if(!(other instanceof ColorConstraint)) {
            return true;
        }
        ColorConstraint cc = (ColorConstraint) other;
        return color == cc.getColor();

    }

    @Override
    public int getIndexValue() {
        return color.ordinal();
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ColorConstraint))
            return false;
        return this.getColor() == ((ColorConstraint) obj).getColor();
    }

}
