package org.poianitibaldizhou.sagrada.game.model;

public enum Direction {
    CLOCKWISE(1), COUNTER_CLOCKWISE(-1);

    private final int increment;

    /**
     * Constructor.
     * Create a Direction that indicates if the next player is the one in the clockwise or counterclockwise direction
     * @param increment the value to increment when changing the index of the player
     */
    Direction(int increment) {
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }
}
