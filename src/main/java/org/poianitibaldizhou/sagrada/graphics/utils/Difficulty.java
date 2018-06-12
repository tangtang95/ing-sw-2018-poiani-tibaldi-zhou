package org.poianitibaldizhou.sagrada.graphics.utils;

public enum Difficulty {
    VERY_EASY(5), EASY(4), MEDIUM(3), HARD(2), HELL(1);

    private final int difficulty;

    Difficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
