package org.poianitibaldizhou.sagrada.graphics.objects;

import org.jetbrains.annotations.Contract;

public enum Color {
    GREEN(0), RED(1), YELLOW(2), BLUE(3), PURPLE(4);

    private int index;

    Color(int index) {
        this.index = index;
    }

    @Contract(pure = true)
    public int getIndex(){
        return index;
    }
}
