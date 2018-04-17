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
        return false;
    }
}
