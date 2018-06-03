package org.poianitibaldizhou.sagrada.graphics.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum WindowSize {
    SMALL(640, 360), MEDIUM(800, 450), BIG(1280, 720);

    private int width;
    private int height;

    WindowSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Contract(pure = true)
    public int getWidth(){
        return width;
    }

    @Contract(pure = true)
    public int getHeight(){
        return height;
    }

    /**
     * @return the name of the enum in lowercase (used to get a substring of css filename)
     */
    @NotNull
    public String getName(){
        return this.name().toLowerCase();
    }

}
