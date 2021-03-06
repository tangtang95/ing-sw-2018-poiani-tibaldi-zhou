package org.poianitibaldizhou.sagrada.graphics.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * OVERVIEW: Contains the list of the resolutions available for the game
 */
public enum WindowSize {
    SMALL(640, 360), MEDIUM(800, 450), BIG(1280, 720);

    private double width;
    private double height;

    /**
     * Resolution of the game.
     *
     * @param width width of the resolution
     * @param height height of the resolution
     */
    WindowSize(double width, double height){
        this.width = width;
        this.height = height;
    }

    /**
     * @return width associated with the enum value
     */
    @Contract(pure = true)
    public double getWidth(){
        return width;
    }

    /**
     * @return height associated with the enum value
     */
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
