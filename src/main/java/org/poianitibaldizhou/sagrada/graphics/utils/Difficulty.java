package org.poianitibaldizhou.sagrada.graphics.utils;

/**
 * OVERVIEW: Represents the various difficulty associated with an integer that represents the level of difficulty of
 * the game
 */
public enum Difficulty {
    VERY_EASY(5), EASY(4), MEDIUM(3), HARD(2), HELL(1);

    private final int difficultyValue;

    /**
     * Private constructor.
     *
     * @param difficulty difficulty level associated with the enum value
     */
    Difficulty(int difficulty) {
        this.difficultyValue = difficulty;
    }

    /**
     * @return difficulty value associated with the enum value
     */
    public int getDifficultyValue() {
        return difficultyValue;
    }
}
