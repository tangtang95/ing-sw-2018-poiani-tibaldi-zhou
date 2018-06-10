package org.poianitibaldizhou.sagrada.graphics.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum WindowSize {
    SMALL(640, 360), MEDIUM(800, 450), BIG(1280, 720);

    private double width;
    private double height;

    WindowSize(double width, double height){
        this.width = width;
        this.height = height;
    }

    @Contract(pure = true)
    public double getWidth(){
        return width;
    }

    @Contract(pure = true)
    public double getHeight(){
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
