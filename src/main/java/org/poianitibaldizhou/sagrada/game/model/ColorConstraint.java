package org.poianitibaldizhou.sagrada.game.model;

public class ColorConstraint implements IConstraint {
    Color color;

    public ColorConstraint(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean matches(IConstraint other) {
        if(!(other instanceof ColorConstraint)) {
            return false;
        }
        ColorConstraint cc = (ColorConstraint) other;
        return color == cc.getColor();

    }

    @Override
    public int getValue() {
        return color.ordinal();
    }
}
